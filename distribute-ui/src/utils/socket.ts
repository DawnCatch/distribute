import WebSocket, { Message } from "tauri-plugin-websocket-api";
import mitt from "mitt";
import { getToken } from "./secure";

const baseURL = "ws://127.0.0.1";

interface SocketOption {
    url: string;
    headers?: Record<string, string>;
    onOpen?: () => void;
    onMessage: (message: Message) => void;
    onClose?: () => void;
}

const socket = (option = {} as SocketOption) => {
    const { url, headers, onOpen, onMessage, onClose } = option;
    const event = mitt();
    onOpen && event.on("onOpen", onOpen);
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
            event.emit("onOpen");
            instance.addListener((message: Message | string) => {
                if (typeof message === "string") {
                    event.emit("onClose");
                } else {
                    event.emit("onMessage", message);
                }
            });
        })
        .catch(() => {
            event.emit("onClose");
        });
};

export { socket };
