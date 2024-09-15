function getTime(time: number) {
  const date = new Date(time)
  let result = ''
  const hours = date.getHours() % 24
  const minutes = date.getMinutes()
  if (hours < 10) result = `0${hours}:`
  else result = `${hours}:`
  if (minutes < 10) result += `0${minutes}`
  else result += `${minutes}`
  return result
}

export { getTime }
