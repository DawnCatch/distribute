// @ts-ignore
const platform = window.__TAURI_IPC__ !== undefined;

const security = import.meta.env.VITE_APP_SECURITY === "true";
const ip = import.meta.env.VITE_APP_IP;

export { platform, security, ip };
