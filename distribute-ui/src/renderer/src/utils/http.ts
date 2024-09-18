import { getToken, setToken } from './secure'
import { ip, port, security } from './env'
import axios, { AxiosRequestConfig } from 'axios'
import { useRelationStore } from '@renderer/stores/relationStore'
import { useAppStore } from '@renderer/stores/appStore'

interface HttpResponse {
  status: boolean
  message: string
  data: Record<string, any> | any
}

const baseURL = `http${security ? 's' : ''}://${ip}:${port}`

function buildUrl(url: string) {
  return baseURL + url
}

// const http = (options = {} as AxiosRequestConfig<any>): Promise<HttpResponse> => {
//   return new Promise((resolve) => {
//     options.url = baseURL + options.url
//     options.headers = {
//       'Content-Type': 'application/json;charset=utf-8',
//       Authorization: getToken(),
//       ...options.headers
//     }
//     if (options.data) options.data = { ...options.data }
//     window.api.http(options).then((res) => {
//       const token = res.headers.authorization
//       if (token) setToken(token)
//       const data = res.data
//       if (!data.status) {
//         console.log(data)
//       }
//       resolve(data)
//     })
//   })
// }

const http = (options = {} as AxiosRequestConfig<any>): Promise<HttpResponse> => {
  return new Promise((resolve) => {
    options.url = baseURL + options.url
    options.headers = {
      Authorization: getToken(),
      ...options.headers
    }
    axios(options).then((res) => {
      const token = res.headers['authorization']
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
    const appStore = useAppStore()
    const message = res.message
    const { type, id: currentId } = appStore.current
    if (message === '关注成功') {
      relationStore.addFollows(id)
    } else if (message === '取消关注') {
      relationStore.removeFollows(id)
      if (!type && id === currentId) appStore.setCurrent(false, -1)
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

export { buildUrl, http, follow, application }
export type { HttpResponse }
