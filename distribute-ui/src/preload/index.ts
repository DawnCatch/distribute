import { contextBridge, ipcRenderer, Notification } from 'electron'
import { electronAPI } from '@electron-toolkit/preload'

// Custom APIs for renderer
const api = {
  http: (option) => {
    console.log(option.data)
    return new Promise((resolve, reject) => {
      const uuid = crypto.randomUUID()
      ipcRenderer.send('http', uuid, option)
      ipcRenderer.on(`${uuid}-resolve`, (_, data) => {
        resolve(data)
      })
      ipcRenderer.on(`${uuid}-reject`, (_, error) => {
        reject(error)
      })
    })
  },
  socket: (option, onMessage: (message: any) => void) => {
    ipcRenderer.send('socket', option)
    ipcRenderer.on('on-message', (_, message) => {
      console.log(message)
      onMessage(message)
    })
  },
  uuid: () => crypto.randomUUID(),
  notification: (title: string, body: string) => {
    if (!Notification.isSupported()) console.error('当前系统不支持通知')
    const ps = typeof title == 'object' ? title : { title, body }
    const n = new Notification(ps)
    n.show()
  }
}

// Use `contextBridge` APIs to expose Electron APIs to
// renderer only if context isolation is enabled, otherwise
// just add to the DOM global.
if (process.contextIsolated) {
  try {
    contextBridge.exposeInMainWorld('electron', electronAPI)
    contextBridge.exposeInMainWorld('api', api)
  } catch (error) {
    console.error(error)
  }
} else {
  // @ts-ignore (define in dts)
  window.electron = electronAPI
  // @ts-ignore (define in dts)
  window.api = api
}
