import { defineStore } from "pinia";
import mitt from "../utils/mitt";

export const useAppStore = defineStore("app", {
    state: () => ({
        profile: {} as Profile,
        messages: [] as Message[],
        relations: [] as Profile[],
        index: -1 as number,
    }),
    actions: {
        setRelations(relations: Profile[]) {
            this.relations = [this.profile, ...relations];
        },
        setProfile(profile: Profile) {
            this.profile = profile;
            this.relations.push(profile);
        },
        addMessage(message: Message) {
            const index = this.messages.findIndex(
                (it: Message) => it.id === message.id
            );
            if (index === -1) {
                this.messages.push(message);
                mitt.emit("rtc:message", message);
            }
        },
        setMessage(messages: Message[]) {
            messages.forEach((it) => {
                this.addMessage(it);
            });
            this.messages.sort((a, b) => (a.date > b.date ? 1 : -1));
        },
        clearMessages() {
            this.messages = [];
        },
        setIndex(index: number) {
            this.index = index;
        },
    },
    getters: {
        messageGroup(state) {
            if (state.messages.length > 0) {
                return state.messages.reduce((group, message) => {
                    const { from, to, date } = message;
                    const session = from === state.profile.userId ? to : from;
                    const key = Math.floor(date / (5 * 60 * 1000));
                    group[session] = group[session] ?? {};
                    group[session][key] = group[session][key] ?? {};
                    group[session][key]!![from] = group[session][key]!![from] ?? [];
                    group[session][key]!![from]!!.push(message);
                    return group;
                }, {} as Record<number, Record<number, Record<number, Message[] | null> | null>>);
            } else return {};
        },
    },
});

interface Profile {
    userId: number;
    nickname: string;
}

interface Message {
    id: number;
    from: number;
    to: number;
    contents: Content[];
    date: number;
}

interface Content {
    type: string;
    value: string;
}

export type { Profile, Message, Content };
