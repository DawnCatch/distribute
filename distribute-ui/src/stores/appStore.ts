import { defineStore } from "pinia";
import { Message } from "tauri-plugin-websocket-api";

export const useAppStore = defineStore("app", {
    state: () => ({
        profile: {} as Profile,
        messages: [] as Message[],
    }),
    actions: {
        setProfile(profile: Profile) {
            this.profile = profile;
        },
        addMessage(message: Message) {
            this.messages.push(message);
        },
    },
    getters: {},
});

interface Profile {
    nickname: string;
}

export type { Profile };
