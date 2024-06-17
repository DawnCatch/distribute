import { defineStore } from "pinia";
import mitt from "../utils/mitt";

export const useAppStore = defineStore("app", {
    state: () => ({
        profile: {} as Profile,
        messages: [] as Message[],
        follows: [] as Relation[],
        fans: [] as Relation[],
        groups: [] as Relation[],
        applications: [] as Relation[],
        profiles: [] as Profile[],
        index: -1 as number,
    }),
    actions: {
        setApplications(applications: Relation[]) {
            this.applications = applications;
        },
        setGroup(groups: Relation[]) {
            this.groups = groups;
        },
        setProfiles(profiles: Profile[]) {
            this.profiles = profiles;
        },
        setFans(fans: Relation[]) {
            this.fans = fans;
        },
        setFollows(follows: Relation[]) {
            this.follows = follows;
        },
        setProfile(profile: Profile) {
            this.profile = profile;
        },
        addMessage(message: Message) {
            const index = this.messages.findIndex(
                (it: Message) => it.id === message.id
            );
            if (index === -1) {
                this.messages.push(message);
                if (message.content.type.indexOf("rtc") !== -1) {
                    mitt.emit("rtc:message", message);
                }
            } else if (message.content.type === "OBSERVER") {
                this.messages[index].observers =
                    this.messages[index].observers ?? [];
                !this.messages[index].observers.includes(message.from) &&
                    this.messages[index].observers.push(message.from);
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
        init() {
            mitt.emit("NavigationDialog:close");
            this.$reset();
        },
    },
    getters: {
        messageGroup(state) {
            if (state.messages.length > 0) {
                return state.messages.reduce((group, message) => {
                    const { type, from, to, date } = message;
                    const typeStr = type ? "Group" : "User";
                    const session = from === state.profile.userId ? to : from;
                    const key = Math.floor(date / (5 * 60 * 1000));
                    group[typeStr] = group[typeStr] ?? {};
                    group[typeStr][session] = group[typeStr][session] ?? {};
                    group[typeStr][session][key] =
                        group[typeStr][session][key] ?? [];
                    const index = group[typeStr][session][key].findIndex(
                        (it) => it.from === from
                    );
                    if (index === -1) {
                        group[typeStr][session][key].push({
                            from: from,
                            messages: [message],
                        } as PairFromMessage);
                    } else {
                        group[typeStr][session][key][index].messages.push(
                            message
                        );
                    }
                    // group[typeStr][session][key]!![from] =
                    //     group[typeStr][session][key]!![from] ?? [];
                    // group[typeStr][session][key]!![from]!!.push(message);
                    return group;
                }, {} as Record<string, Record<number, Record<number, PairFromMessage[] | null>>>);
                // }, {} as Record<string, Record<number, Record<number, Record<number, Message[] | null> | null>>>);
            } else return {};
        },
        relations(state) {
            const fans = new Set(state.fans.map((item) => item.id));
            return state.follows
                .filter((item) => fans.has(item.id))
                .concat(state.groups) as Relation[];
        },
    },
});

interface Relation {
    type: boolean;
    id: number;
    title: String;
    path: String;
}

interface Profile {
    userId: number;
    nickname: string;
}

interface Message {
    type: boolean;
    id: number;
    from: number;
    to: number;
    content: Content;
    date: number;
    observers: number[];
}

interface Content {
    type: string;
    value: string;
}

interface PairFromMessage {
    from: number;
    messages: Message[];
}

export type { Relation, Profile, Message, Content, PairFromMessage };
