import TauriWebSocket, {
    Message as TauriMessage,
} from "tauri-plugin-websocket-api";
import mitt from "mitt";
import { getToken } from "./secure";
import { ip, platform, port, proxy_rewrite, security } from "./env";
import { Message } from "../stores/appStore";

const baseURL = `ws${security ? "s" : ""}://${ip}${
    port !== "" ? `:${port}` : ""
}`;

interface SocketOption {
    url: string;
    headers?: Record<string, string>;
    onOpen?: (instance: TauriWebSocket | WebSocket) => void;
    onMessage: (message: Message) => void;
    onClose?: () => void;
}

const socket = (option = {} as SocketOption) => {
    const { url, headers, onOpen, onMessage, onClose } = option;
    const event = mitt();
    event.on("onOpen", (instance) => {
        onOpen && onOpen(instance as WebSocket);
    });
    event.on("onMessage", (message) => {
        onMessage && onMessage(message as Message);
    });
    event.on("onClose", () => {
        onClose && onClose();
        setTimeout(() => {
            socket(option);
        }, 2000);
    });
    if (platform) {
        console.log(baseURL + url);
        TauriWebSocket.connect(baseURL + url, {
            headers: {
                "Sec-Websocket-Protocol": getToken(),
                ...headers,
            },
        })
            .then((instance: TauriWebSocket) => {
                event.emit("onOpen", instance);
                instance.addListener((message: TauriMessage | string) => {
                    if (typeof message === "string") {
                        event.emit("onClose");
                    } else {
                        const data = JSON.parse(
                            message.data as string
                        ) as Message;
                        event.emit("onMessage", data);
                    }
                });
            })
            .catch(() => {
                event.emit("onClose");
            });
    } else {
        const ws = new WebSocket(baseURL + url, getToken());
        ws.onopen = () => {
            event.emit("onOpen", ws);
        };
        ws.onmessage = (e) => {
            const data = JSON.parse(e.data as string) as Message;
            event.emit("onMessage", data);
        };
        ws.onclose = () => {
            event.emit("onClose");
        };
    }
};

export { socket };
