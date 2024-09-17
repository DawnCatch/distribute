import { getToken, setToken } from './secure'
import { ip, port, security } from './env'
import { AxiosRequestConfig } from 'axios'
import { useRelationStore } from '@renderer/stores/relationStore'

interface HttpResponse {
  status: boolean
  message: string
  data: Record<string, any>
}

const baseURL = `http${security ? 's' : ''}://${ip}:${port}`

const http = (options = {} as AxiosRequestConfig): Promise<HttpResponse> => {
  return new Promise((resolve) => {
    options.url = baseURL + options.url
    options.headers = {
      'Content-Type': 'application/json;charset=utf-8',
      Authorization: getToken(),
      ...options.headers
    }
    if (options.data) options.data = { ...options.data }
    window.api.http(options).then((res) => {
      const token = res.headers.authorization
      if (token) setToken(token)
      const data = res.data
      if (!data.status) {
        console.log(data)
      }
      resolve(data)
    })
  })
}

function follow(id: number) {
  http({
    method: 'POST',
    url: '/relation/user/follow',
    data: {
      targetId: id
    }
  }).then((res) => {
    if (!res.status) return
    const relationStore = useRelationStore()
    const message = res.message
    if (message === '关注成功') {
      relationStore.addFollows(id)
    } else if (message === '取消关注') {
      relationStore.removeFollows(id)
    }
  })
}

function application(id: number) {
  http({
    method: 'POST',
    url: '/relation/group/application',
    data: {
      targetId: id
    }
  }).then((res) => {
    if (!res.status) return
    const relationStore = useRelationStore()
    const message = res.message
    if (message === '申请成功') {
      relationStore.addFollows(id)
    } else if (message === '取消成功') {
      relationStore.removeFollows(id)
    }
  })
}

export { http, follow, application }
export type { HttpResponse }
