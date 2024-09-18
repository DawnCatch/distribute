import { http } from '@renderer/utils/http'
import { defineStore } from 'pinia'

export const useRelationStore = defineStore('relation', {
  state: () => ({
    follows: [] as number[],
    fans: [] as number[],
    groups: [] as number[],
    applications: [] as number[],
    pends: [] as number[],
    relations: [] as Relation[],
    membersGroup: {} as Record<number, number>,
    current: {
      type: false,
      id: -1
    }
  }),
  actions: {
    setUnion(union: Union) {
      this.follows = union.follows
      this.fans = union.fans
      this.groups = union.groups
      this.applications = union.applications
      this.pends = union.pends
    },
    addFollow(id: number) {
      if (this.follows.includes(id)) return
      this.follows.push(id)
    },
    removeFollow(id: number) {
      const index = this.follows.findIndex((it) => it === id)
      if (index === -1) return
      this.follows.splice(index, 1)
    },
    addFan(id: number) {
      if (this.fans.includes(id)) return
      this.fans.push(id)
    },
    removeFan(id: number) {
      const index = this.fans.findIndex((it) => it === id)
      if (index === -1) return
      this.fans.splice(index, 1)
    },
    addGroup(id: number) {
      if (this.groups.includes(id)) return
      this.groups.push(id)
    },
    removeGroup(id: number) {
      const index = this.groups.findIndex((it) => it === id)
      if (index === -1) return
      this.groups.splice(index, 1)
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
    addPend(id: number) {
      if (this.pends.includes(id)) return
      this.pends.push(id)
    },
    removePend(id: number) {
      const index = this.pends.findIndex((it) => it === id)
      if (index === -1) return
      this.pends.splice(index, 1)
    },
    getRelation(type: boolean, id: number) {
      http({
        method: 'POST',
        url: '/relation/get',
        data: {
          type,
          id
        }
      }).then((res) => {
        if (!res.status) return
        this.addRelation(res.data as Relation)
        if (type) this.getMember(res.data.targetId)
      })
    },
    getRelationByTarget(type: boolean, id: number) {
      http({
        method: 'POST',
        url: '/relation/get/target',
        data: {
          type,
          id
        }
      }).then((res) => {
        if (!res.status) return
        this.addRelation(res.data as Relation)
        if (type) this.getMember(res.data.targetId)
      })
    },
    addRelation(relation: Relation) {
      let index = -1
      if (relation.id === 0) {
        index = this.relations.findIndex(
          (it) => it.targetId === relation.targetId && it.type === relation.type
        )
      } else {
        index = this.relations.findIndex((it) => it.id === relation.id && it.type === relation.type)
      }
      if (index === -1) {
        this.relations.push(relation)
      } else {
        this.relations[index] = relation
      }
    },
    addRelations(relations: Relation[]) {
      relations.forEach((it) => this.addRelation(it))
    },
    getMember(id: number) {
      http({
        url: `/relation/len/member/${id}`
      }).then((res) => {
        if (!res.status) return
        this.membersGroup[id] = Number(res.data)
      })
    },
    getGroups() {
      http({
        url: '/relation/list/group'
      }).then((res) => {
        if (!res.status) return
        this.groups = res.data
      })
    },
    setCurrent(type: boolean, id: number) {
      this.current.type = type
      this.current.id = id
    },
    setGroupAvatar(id: number, url: string) {
      if (id === -1 || url === '') return
      const index = this.relations.findIndex((it) => it.type && it.targetId === id)
      if (index === -1) return
      this.relations[index].avatarUrl = url
    }
  },
  getters: {
    members() {
      return (id: number): number => {
        return this.membersGroup[id]
      }
    },
    frients(state) {
      return state.follows.filter((it) => state.fans.includes(it))
    },
    ownRelations(): RelationQuery[] {
      return this.frients
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
    },
    relation() {
      return (type: boolean, id: number): Relation | undefined => {
        return this.relations.find((it) => it.type === type && it.id === id)
      }
    },
    relationByTarget() {
      return (type: boolean, id: number): Relation | undefined => {
        return this.relations.find((it) => it.type === type && it.targetId === id)
      }
    },
    link() {
      return (type: boolean, id: number) => {
        if (type) {
          let relation = '!'
          if (this.groups.includes(id)) {
            relation = '='
          } else if (this.applications.includes(id)) {
            relation = '>'
          }
          return relation
        } else {
          let relation = '!'
          if (this.frients.includes(id)) {
            relation = '='
          } else if (this.follows.includes(id)) {
            relation = '>'
          } else if (this.fans.includes(id)) {
            relation = '<'
          }
          return relation
        }
      }
    }
  }
})