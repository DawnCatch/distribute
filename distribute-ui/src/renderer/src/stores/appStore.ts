import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    ownId: -1,
    profiles: [] as Profile[],
    follows: [] as number[],
    fans: [] as number[],
    groups: [] as number[],
    relations: [] as Relation[],
    messages: [] as Message[],
    messageMap: [] as Message[][][],
    messageGroup: {} as Record<number, Record<number, Message[][]>>[],
    current: {
      type: false,
      id: -1
    }
  }),
  actions: {
    addProfile(profile: Profile) {
      const index = this.profiles.findIndex((it) => it.userId === profile.userId)
      if (index === -1) {
        this.profiles.push(profile)
      } else {
        this.profiles[index] = profile
      }
    },
    setOwn(profile: Profile) {
      this.ownId = profile.userId
      this.addProfile(profile)
    },
    addProfiles(profiles: Profile[]) {
      profiles.forEach((it) => this.addProfile(it))
    },
    addRelation(relation: Relation) {
      const index = this.relations.findIndex(
        (it) => it.id === relation.id && it.type === relation.type
      )
      if (index === -1) {
        this.relations.push(relation)
      } else {
        this.relations[index] = relation
      }
    },
    addRelations(relations: Relation[]) {
      relations.forEach((it) => this.addRelation(it))
    },
    setUnion(union: Union) {
      this.follows = union.follows.map((it) => it.id)
      this.fans = union.fans
      this.groups = union.groups.map((it) => it.id)
      this.addRelations(union.follows.concat(union.groups))
    },
    getRelation(type: boolean, id: number): Relation | undefined {
      return this.relations.find((it) => it.type === type && it.id === id)
    },
    addMessage(message: Message) {
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
        session = from === this.ownId ? to : from
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
      return state.ownId !== -1
    },
    frients(state) {
      return state.follows.filter((value) => state.fans.includes(value))
    },
    ownRelations(): Relation[] {
      const result = [] as Relation[]
      this.frients
        .map((it) => {
          return {
            type: false,
            id: it
          }
        })
        .concat(
          this.groups.map((it) => {
            return {
              type: true,
              id: it
            }
          })
        )
        .forEach((it) => {
          const relation = this.relation(it.type, it.id)
          relation && result.push(relation)
        })
      return result
    },
    profile() {
      return (id: number): Profile | undefined => {
        return this.profiles.find((it) => it.userId === id)
      }
    },
    relation() {
      return (type: boolean, id: number): Relation | undefined => {
        return this.relations.find((it) => it.type === type && it.id === id)
      }
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
    currentItem(): ProfileData {
      const { type, id } = this.current
      const relation = this.relation(type, id) ?? { type, id, title: '', path: '/' }
      if (!type) {
        const profile = this.profile(id) ?? { userId: id, nickname: '' }
        return {
          type,
          id,
          title: relation.title,
          path: relation.path,
          nickname: profile.nickname
        }
      } else {
        return {
          type,
          id,
          title: relation.title,
          path: relation.path,
          nickname: ''
        }
      }
    },
    currentTitle(): string {
      const data = this.currentItem
      if (data.title === '' && data.nickname === '') return '*None'
      if (data.title !== '') return data.title
      return data.nickname
    },
    own(): Profile {
      const profile = this.profile(this.ownId) ?? { userId: this.ownId, nickname: '*None' }
      return profile
    }
  }
})

interface Profile {
  userId: number
  nickname: string
}

interface Relation {
  type: boolean
  id: number
  title: string
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
  follows: Relation[]
  fans: number[]
  groups: Relation[]
}

interface ProfileData {
  type: boolean
  id: number
  title: string
  nickname: string
  path: string
}

export type { Profile, Message, Content, Relation, Union }
