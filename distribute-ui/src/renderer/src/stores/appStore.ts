import { defineStore } from 'pinia'
import { useRelationStore } from './relationStore'

export const useAppStore = defineStore('app', {
  state: () => ({
    own: {
      userId: -1,
      nickname: 'None',
      avatarUrl: ''
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
    setAvatar(url: string) {
      this.own.avatarUrl = url
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
        const symbol = value[0]
        let id = 0
        try {
          id = Number(value.slice(1))
        } catch (e) {
          id = Number(value)
        }
        if (args[1] === 'FAN') {
          if (symbol === '+') {
            relationStore.addFan(id)
          } else {
            relationStore.removeFan(id)
          }
        } else if (args[1] === 'GROUP') {
          if (symbol === '+') {
            relationStore.addGroup(id)
          } else {
            relationStore.removeGroup(id)
          }
        } else if (args[1] === 'PENDING') {
          if (symbol === '+') {
            relationStore.addPend(id)
          } else {
            relationStore.removePend(id)
          }
        } else if (args[1] === 'APPLICATION') {
          if (symbol === '+') {
            relationStore.addApplication(id)
          } else {
            relationStore.removePend(id)
          }
        } else if (args[1] === 'MEMBER') {
          relationStore.getMember(id)
        }
      }
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
      const result = relationStore.relationByTarget(type, id)
      if (result === undefined) relationStore.getRelationByTarget(type, id)
      return result
    }
  }
})
