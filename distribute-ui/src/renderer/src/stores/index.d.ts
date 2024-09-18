interface UserProfile {
  userId: number
  nickname: string
  avatarUrl: string
}

interface Relation {
  id: number
  type: boolean
  userId: number
  targetId: number
  title: string
  nickname: string
  avatarUrl: string
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

interface RelationQuery {
  type: boolean
  id: number
}
