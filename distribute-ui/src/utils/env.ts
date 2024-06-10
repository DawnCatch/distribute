// @ts-ignore
const platform = window.__TAURI_IPC__ !== undefined;

const security = import.meta.env.VITE_APP_SECURITY === "true";
const ip = import.meta.env.VITE_SERVER_IP;
const port = import.meta.env.VITE_SERVER_PORT;

const proxy_rewrite = import.meta.env.VITE_PROXY_REWRITE;

export { platform, security, ip, port, proxy_rewrite };
