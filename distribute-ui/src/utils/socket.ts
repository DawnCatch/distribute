import WebSocket, { Message as TauriMessage } from "tauri-plugin-websocket-api";
import mitt from "mitt";
import { getToken } from "./secure";
import { ip, security } from "./env";
import { Message } from "../stores/appStore";

const baseURL = `ws${security ? "s" : ""}://${ip}`;

interface SocketOption {
    url: string;
    headers?: Record<string, string>;
    onOpen?: (instance: WebSocket) => void;
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
    WebSocket.connect(baseURL + url, {
        headers: {
            Authorization: getToken(),
            ...headers,
        },
    })
        .then((instance: WebSocket) => {
            event.emit("onOpen", instance);
            instance.addListener((message: TauriMessage | string) => {
                if (typeof message === "string") {
                    event.emit("onClose");
                } else {
                    const data = JSON.parse(message.data as string) as Message;
                    event.emit("onMessage", data);
                }
            });
        })
        .catch(() => {
            event.emit("onClose");
        });
};

export { socket };
