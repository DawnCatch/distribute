import { defineStore } from 'pinia'

export const useChatStore = defineStore('app', {
  state: () => ({
    ownId: Number,
    profiles: [] as Profile[]
  }),
  actions: {},
  getters: {}
})

interface Profile {
  userId: number
  nickname: string
}
