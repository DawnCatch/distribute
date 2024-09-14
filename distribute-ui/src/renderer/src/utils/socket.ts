import { ip, port } from './env'
import mitt from './mitt'
import { getToken } from './secure'

const url = `ws://${ip}:${port}/chat`

function socket() {
  window.api.socket(
    {
      url: url,
      protocols: getToken()
    },
    (message) => {
      mitt.emit('on-message', message)
    }
  )
}

export default socket
