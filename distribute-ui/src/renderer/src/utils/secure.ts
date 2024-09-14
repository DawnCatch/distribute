function getToken(): string {
  return window.localStorage.getItem('token') || ''
}

function setToken(value: string): void {
  window.localStorage.setItem('token', value)
}

export { getToken, setToken }
