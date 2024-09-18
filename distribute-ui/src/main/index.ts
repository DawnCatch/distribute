import { app, shell, BrowserWindow, ipcMain } from 'electron'
import { join } from 'path'
import { electronApp, optimizer, is } from '@electron-toolkit/utils'
import icon from '../../resources/icon.png?asset'
import axios from 'axios'
import WebSocketClient from './web-socket-client'

function createWindow(): void {
  // Create the browser window.
  const mainWindow = new BrowserWindow({
    width: 1130,
    height: 670,
    show: false,
    frame: false,
    autoHideMenuBar: true,
    ...(process.platform === 'linux' ? { icon } : {}),
    webPreferences: {
      preload: join(__dirname, '../preload/index.js'),
      sandbox: false
    }
  })

  mainWindow.setMenu(null)

  mainWindow.on('ready-to-show', () => {
    mainWindow.show()
  })

  mainWindow.webContents.setWindowOpenHandler((details) => {
    shell.openExternal(details.url)
    return { action: 'deny' }
  })

  // HMR for renderer base on electron-vite cli.
  // Load the remote URL for development or the local html file for production.
  if (is.dev && process.env['ELECTRON_RENDERER_URL']) {
    mainWindow.loadURL(process.env['ELECTRON_RENDERER_URL'])
  } else {
    mainWindow.loadFile(join(__dirname, '../renderer/index.html'))
  }
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.whenReady().then(() => {
  // Set app user model id for windows
  electronApp.setAppUserModelId('com.electron')

  // Default open or close DevTools by F12 in development
  // and ignore CommandOrControl + R in production.
  // see https://github.com/alex8088/electron-toolkit/tree/master/packages/utils
  app.on('browser-window-created', (_, window) => {
    optimizer.watchWindowShortcuts(window)
  })

  // IPC test
  ipcMain.on('ping', () => console.log('pong'))

  createWindow()

  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
})

// Quit when all windows are closed, except on macOS. There, it's common
// for applications and their menu bar to stay active until the user quits
// explicitly with Cmd + Q.
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

// In this file you can include the rest of your app"s specific main process
// code. You can also put them in separate files and require them here.
ipcMain.on('window-min', function () {
  BrowserWindow.getFocusedWindow()?.minimize()
})
ipcMain.on('window-max', function () {
  const mainWindow = BrowserWindow.getFocusedWindow()
  if (mainWindow === null) return
  if (mainWindow.isMaximized()) {
    mainWindow.restore()
  } else {
    mainWindow.maximize()
  }
})
ipcMain.on('window-close', function () {
  BrowserWindow.getFocusedWindow()?.close()
})

ipcMain.on('http', async function (event, uuid, option) {
  const data = new FormData()
  for (const key in option.data) {
    data.append(key, option.data[key])
  }
  option.data = data
  for (let i = 1; i <= 5; i++) {
    try {
      const res = await axios(option)
      event.sender.send(`${uuid}-resolve`, {
        data: res.data,
        headers: res.headers,
        status: res.status
      })
      return
    } catch (error) {
      event.sender.send(`${uuid}-reject`, `请求失败(第${i}次):${option.url}`)
    }
  }
})

let webSocketClient: WebSocketClient | null = null
ipcMain.on('socket', function (event, option) {
  option.onMessage = (message: any) => {
    event.sender.send('on-message', message)
  }
  if (webSocketClient !== null) {
    webSocketClient.option = option
    return
  }
  webSocketClient = new WebSocketClient(option)
  webSocketClient.connect()
})
