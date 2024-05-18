import { defineStore } from "pinia";

export const useAppStore = defineStore("app", {
    state: () => ({
        profile: { nickname: "zhou03" } as Profile,
    }),
    actions: {
        setProfile(profile: Profile) {
            this.profile = profile;
        },
    },
    getters: {},
});

interface Profile {
    nickname: string;
}

export type { Profile };
