import {
    isPermissionGranted,
    Options,
    requestPermission,
    sendNotification,
} from "@tauri-apps/api/notification";

const permission = async (): Promise<boolean> => {
    let permissionGranted = await isPermissionGranted();
    if (!permissionGranted) {
        const permission = await requestPermission();
        permissionGranted = permission === "granted";
    }
    return permissionGranted;
};

type Notification = (options: Options | string) => void;

const notification = (options: Options | string): void => {
    permission().then((value) => {
        value && sendNotification(options);
    });
};

export { notification };
export type { Notification };
