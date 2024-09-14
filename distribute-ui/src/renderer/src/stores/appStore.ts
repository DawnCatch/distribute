import { defineStore } from 'pinia'
import mitt from '../utils/mitt'

export const useAppStore = defineStore('app', {
  state: () => ({
    profile: {} as Profile,
    messages: [] as Message[],
    follows: [] as number[],
    fans: [] as number[],
    groups: [] as number[],
    profiles: [] as Relation[],
    checkItem: {
      type: false,
      id: -1,
      title: 'None',
      path: '/'
    } as Relation
  }),
  actions: {
    setProfile(profile: Profile) {
      this.profile = profile
    },
    setUnion(union: Union) {
      this.follows = union.follows.map((it) => it.id)
      this.fans = union.fans
      this.groups = union.groups.map((it) => it.id)
      this.addProfiles(union.follows.concat(union.groups))
    },
    addProfile(profile: Relation) {
      const index = this.profiles.findIndex(
        (it) => it.id === profile.id && it.type === profile.type
      )
      if (index === -1) {
        this.profiles.push(profile)
      } else {
        this.profiles[index] = { ...profile }
      }
    },
    addProfiles(profiles: Relation[]) {
      profiles.forEach((it) => {
        this.addProfile(it)
      })
    },
    addMessage(message: Message) {
      const index = this.messages.findIndex((it: Message) => it.id === message.id)
      if (index === -1) {
        this.messages.push(message)
        if (message.content.type.indexOf('rtc') !== -1) {
          mitt.emit('rtc:message', message)
        }
      } else if (
        message.content.type === 'OBSERVER' &&
        !this.messages[index].observers.includes(message.from)
      ) {
        this.messages[index].observers = this.messages[index].observers ?? []
        this.messages[index].observers.push(message.from)
      }
    },
    setMessage(messages: Message[]) {
      messages.forEach((it) => {
        this.addMessage(it)
      })
      this.messages.sort((a, b) => (a.date > b.date ? 1 : -1))
    },
    clearMessages() {
      this.messages = []
    },
    check(item: Relation) {
      this.checkItem = item
    }
  },
  getters: {
    messageGroup(state) {
      if (state.messages.length > 0) {
        return state.messages.reduce(
          (group, message) => {
            const { from, to, type, date } = message
            const work = type ? 1 : 0
            let session: number
            let key: number
            if (type) {
              session = to
              key = Math.floor(date / (5 * 60 * 1000))
            } else {
              session = from === state.profile.userId ? to : from
              key = Math.floor(date / (5 * 60 * 1000))
            }
            group[work] = group[work] ?? {}
            group[work][session] = group[work][session] ?? {}
            group[work][session][key] = group[work][session][key] ?? []
            const messagesArray = group[work][session][key]
            if (
              messagesArray.length === 0 ||
              messagesArray[messagesArray.length - 1][0].from !== from
            ) {
              messagesArray.push([message] as Message[])
            } else {
              messagesArray[messagesArray.length - 1].push(message)
            }
            return group
          },
          {} as Record<number, Record<number, Message[][]>>[]
        )
      } else return {}
    },
    relations(state) {
      const friendIds = state.follows.filter((value) => state.fans.includes(value))
      const friends = state.profiles.filter((it) => !it.type && friendIds.includes(it.id))
      const groups = state.profiles.filter((it) => it.type && state.groups.includes(it.id))
      return friends.concat(groups)
    }
  }
})

interface Profile {
  userId: number
  nickname: string
}

interface Message {
  id: number
  type: boolean
  from: number
  to: number
  content: Content
  date: number
  observers: number[]
}

interface Content {
  type: string
  value: string
}

interface Relation {
  type: boolean
  id: number
  title: string
  path: string
}

interface Union {
  follows: Relation[]
  fans: number[]
  groups: Relation[]
}
export type { Profile, Message, Content }
