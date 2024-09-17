import { getToken, setToken } from './secure'
import { ip, port, security } from './env'
import { AxiosRequestConfig } from 'axios'

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

export { http }
export type { HttpResponse }
