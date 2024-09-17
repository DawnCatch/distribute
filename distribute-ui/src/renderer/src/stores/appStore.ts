import { defineStore } from 'pinia'
import { useRelationStore } from './relationStore'

export const useAppStore = defineStore('app', {
  state: () => ({
    own: {
      userId: -1,
      nickname: 'None'
    } as UserProfile,
    messages: [] as Message[],
    messageMap: [] as Message[][][],
    messageGroup: [] as Record<number, Record<number, Message[][]>>[],
    current: {
      type: false,
      id: -1
    }
  }),
  actions: {
    setOwn(userProfile: UserProfile) {
      this.own = userProfile
    },
    addMessage(message: Message) {
      if (message.from === 0) return this.handleSystemNotice(message)
      const index = this.messages.findIndex((it) => it.id === message.id)
      if (index === -1) {
        this.messages.push(message)
        this.buildMessageGroup(message)
      } else if (
        message.content.type === 'OBSERVER' &&
        !this.messages[index].observers.includes(message.from)
      ) {
        this.messages[index].observers = this.messages[index].observers ?? []
        this.messages[index].observers.push(message.from)
      } else {
        this.messages[index] = message
      }
    },
    handleSystemNotice(message: Message) {
      const relationStore = useRelationStore()
      const { content } = message
      const { type, value } = content
      const args = type.split(':')
      if (args[0] === 'RELATION') {
        if (args[1] === 'FANS') {
          const id = Number(value.slice(1))
          if (value[0] === '+') {
            relationStore.addFans(id)
          } else {
            relationStore.removeFans(id)
          }
        } else if (args[1] === 'PENDING') {
          const id = Number(value.slice(1))
          if (value[0] === '+') {
            relationStore.addPend(id)
          } else {
            relationStore.removePend(id)
          }
        }
      }
      console.log(content)
    },
    addMessages(messages: Message[]) {
      messages.forEach((it) => this.addMessage(it))
    },
    buildMessageGroup(message: Message) {
      const { from, to, type, date } = message
      const work = type ? 1 : 0
      let session: number
      let key: number
      if (type) {
        session = to
        key = Math.floor(date / (5 * 60 * 1000))
      } else {
        session = from === this.own.userId ? to : from
        key = Math.floor(date / (5 * 60 * 1000))
      }
      this.messageMap = this.messageMap ?? []
      this.messageMap[work] = this.messageMap[work] ?? {}
      this.messageMap[work][session] = this.messageMap[work][session] ?? []
      this.messageMap[work][session].push(message)

      const group = (this.messageGroup = this.messageGroup ?? [])
      group[work] = group[work] ?? {}
      group[work][session] = group[work][session] ?? {}
      group[work][session][key] = group[work][session][key] ?? []
      const messagesArray = group[work][session][key]
      if (messagesArray.length === 0 || messagesArray[messagesArray.length - 1][0].from !== from) {
        messagesArray.push([message] as Message[])
      } else {
        messagesArray[messagesArray.length - 1].push(message)
      }
    },
    setCurrent(type: boolean, id: number) {
      this.current.type = type
      this.current.id = id
    }
  },
  getters: {
    isLogin(state) {
      return state.own.userId !== -1
    },
    group() {
      return (type, id) => {
        if (id === -1) return {}
        return this.messageGroup[type ? 1 : 0][id]
      }
    },
    currentGroup(): Record<number, Message[][]> {
      const { type, id } = this.current
      return this.group(type, id)
    },
    currentItem(): Relation | undefined {
      const relationStore = useRelationStore()
      const { type, id } = this.current
      const result = relationStore.relation(type, id)
      if (result === undefined) relationStore.getRelation(type, id)
      return result
    }
  }
})

interface UserProfile {
  userId: number
  nickname: string
}

interface Relation {
  id: number
  type: boolean
  targetId: number
  title: string
  nickname: string
  role: string
  path: string
}

interface Content {
  type: string
  value: string
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

interface Union {
  follows: number[]
  fans: number[]
  groups: number[]
  applications: number[]
  pends: number[]
}

export type { UserProfile, Message, Content, Relation, Union }
