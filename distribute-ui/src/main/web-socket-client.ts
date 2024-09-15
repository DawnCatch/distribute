import WebSocket from 'ws'

interface WebSocketOption {
  url: string
  protocols: string | string[]
  reconnectDelay: number
  onMessage: (message: any) => void | null
}

class WebSocketClient {
  isClose = false
  option: WebSocketOption
  ws: WebSocket | null
  isConnected: boolean

  constructor(option = {} as WebSocketOption) {
    this.option = option
    this.option.reconnectDelay = this.option.reconnectDelay || 1000
    this.ws = null
    this.isConnected = false
  }

  connect() {
    this.isClose = false
    this.ws = new WebSocket(this.option.url, this.option.protocols)

    this.ws.onopen = () => {
      this.isConnected = true
      console.log('Connection established')
    }

    this.ws.onmessage = (event) => {
      console.log('Message received: ' + event.data)
      const data = JSON.parse(event.data as string)
      this.option.onMessage && this.option.onMessage(data)
    }

    this.ws.onclose = () => {
      this.isConnected = false
      console.log('Connection closed')
      this.reconnect()
    }

    this.ws.onerror = (event) => {
      console.error('WebSocket error: ' + JSON.stringify(event))
      this.ws?.close()
    }
  }

  reconnect() {
    if (!this.isConnected && !this.isClose) {
      console.log(`Reconnecting in ${this.option.reconnectDelay / 1000} seconds...`)
      setTimeout(() => {
        this.connect()
      }, this.option.reconnectDelay)
    }
  }

  close() {
    this.isClose = true
    this.ws?.close()
  }

  getStatus() {
    return this.isConnected || false
  }
}

export default WebSocketClient
