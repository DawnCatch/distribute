import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    ownId: -1,
    userProfiles: [] as UserProfile[],
    groupProfiles: [] as GroupProfile[],
    follows: [] as number[],
    fans: [] as number[],
    groups: [] as number[],
    applications: [] as number[],
    relations: [] as Relation[],
    messages: [] as Message[],
    messageMap: [] as Message[][][],
    messageGroup: [] as Record<number, Record<number, Message[][]>>[],
    current: {
      type: false,
      id: -1
    }
  }),
  actions: {
    addFollows(id: number) {
      if (this.follows.includes(id)) return
      this.follows.push(id)
    },
    removeFollows(id: number) {
      const index = this.follows.findIndex((it) => it === id)
      if (index === -1) return
      this.follows.splice(index, 1)
    },
    addApplication(id: number) {
      if (this.applications.includes(id)) return
      this.applications.push(id)
    },
    removeApplication(id: number) {
      const index = this.applications.findIndex((it) => it === id)
      if (index === -1) return
      this.applications.splice(index, 1)
    },
    addUserProfile(userProfile: UserProfile) {
      const index = this.userProfiles.findIndex((it) => it.userId === userProfile.userId)
      if (index === -1) {
        this.userProfiles.push(userProfile)
      } else {
        this.userProfiles.splice(index, 1, userProfile)
      }
    },
    setOwn(userProfile: UserProfile) {
      this.ownId = userProfile.userId
      this.addUserProfile(userProfile)
    },
    addUserProfiles(userProfiles: UserProfile[]) {
      userProfiles.forEach((it) => this.addUserProfile(it))
    },
    addGroupProfile(groupProfile: GroupProfile) {
      const index = this.groupProfiles.findIndex((it) => it.groupId === groupProfile.groupId)
      if (index === -1) {
        this.groupProfiles.push(groupProfile)
      } else {
        this.groupProfiles.splice(index, 1, groupProfile)
      }
    },
    addGroupProfiles(groupProfiles: GroupProfile[]) {
      groupProfiles.forEach((it) => this.addGroupProfile(it))
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
      this.applications = union.applications.map((it) => it.id)
      this.addRelations(union.follows.concat(union.groups).concat(union.applications))
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
    userProfile() {
      return (id: number): UserProfile | undefined => {
        return this.userProfiles.find((it) => it.userId === id)
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
        const profile = this.userProfile(id) ?? { userId: id, nickname: '' }
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
    own(): UserProfile {
      const profile = this.userProfile(this.ownId) ?? { userId: this.ownId, nickname: '*None' }
      return profile
    },
    profiles(state): Profile[] {
      let result = [] as Profile[]
      const userIds = [
        ...new Set([...state.follows, ...state.fans, ...state.userProfiles.map((it) => it.userId)])
      ]
      result = result.concat(
        userIds.map((id) => {
          const profile = state.userProfiles.find((it) => it.userId === id)
          let relation = '!'
          if (state.follows.filter((value) => state.fans.includes(value)).includes(id)) {
            relation = '='
          } else if (state.follows.includes(id)) {
            relation = '>'
          } else if (state.fans.includes(id)) {
            relation = '<'
          }
          return {
            type: false,
            id,
            title: profile?.nickname ?? 'None',
            relation
          }
        })
      )
      const groupIds = [
        ...new Set([
          ...state.groups,
          ...state.applications,
          ...state.groupProfiles.map((it) => it.groupId)
        ])
      ]
      result = result.concat(
        groupIds.map((id) => {
          const profile = state.groupProfiles.find((it) => it.groupId === id)
          let relation = '!'
          if (state.groups.includes(id)) {
            relation = '='
          } else if (state.applications.includes(id)) {
            relation = '>'
          }
          return {
            type: true,
            id,
            title: profile?.title ?? 'None',
            relation
          }
        })
      )
      return result
    },
    profile() {
      return (type: boolean, id: number): Profile => {
        if (type) {
          const profile = this.groupProfiles.find((it) => it.groupId === id)
          let relation = '!'
          if (this.groups.includes(id)) {
            relation = '='
          } else if (this.applications.includes(id)) {
            relation = '>'
          }
          return {
            type,
            id,
            title: profile?.title ?? 'None',
            relation
          }
        } else {
          const profile = this.userProfiles.find((it) => it.userId === id)
          let relation = '!'
          if (this.frients.includes(id)) {
            relation = '='
          } else if (this.follows.includes(id)) {
            relation = '>'
          } else if (this.fans.includes(id)) {
            relation = '<'
          }
          return {
            type,
            id,
            title: profile?.nickname ?? 'None',
            relation
          }
        }
      }
    }
  }
})

interface UserProfile {
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
  applications: Relation[]
}

interface ProfileData {
  type: boolean
  id: number
  title: string
  nickname: string
  path: string
}

interface GroupProfile {
  groupId: number
  title: string
  visible: boolean
  createDate: number
}

interface Profile {
  type: boolean
  id: number
  title: string
  relation: string
}

export type { UserProfile, GroupProfile, Message, Content, Relation, Union, Profile }
