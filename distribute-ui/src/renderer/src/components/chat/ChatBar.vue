<template>
  <div v-if="appStore.checkItem.id !== -1" class="chat_bar">
    <div class="message_list" :style="messageListStyle">
      <div @click="send">start</div>
      <div v-for="(item, index) in group" :key="index" class="message_content">
        <div class="time_stamp">
          {{ getTime(item) }}
        </div>
        <Message
          v-for="messages in item as Record<number, MessageModel[]>"
          :key="messages"
          :messages="messages"
        />
      </div>
    </div>
    <ChatoptionBar ref="chatOptionBar" />
  </div>
</template>

<script setup lang="ts">
import { Message as MessageModel, useAppStore } from '../../stores/appStore'
import { computed, ref } from 'vue'
import mitt from '../../utils/mitt'
import { useElementSize } from '@vueuse/core'

import Message from './Message.vue'
import ChatoptionBar from './ChatOptionBar.vue'

const appStore = useAppStore()

const group = computed(() => {
  const { type, id } = appStore.checkItem
  if (id === -1) return {}
  try {
    const result = appStore.messageGroup[type ? 1 : 0][id]
    return result
  } catch (e) {
    return {}
  }
})

function send() {
  mitt.emit('rtc:request', appStore.relations[appStore.index].userId)
}

function getTime(item: Record<number, MessageModel[]>) {
  for (const key in item) {
    const date = new Date(item[key][0].date)
    let result = ''
    const hours = date.getHours() % 24
    const minutes = date.getMinutes()
    if (hours < 10) result = `0${hours}:`
    else result = `${hours}:`
    if (minutes < 10) result += `0${minutes}`
    else result += `${minutes}`
    return result
  }
}

const chatOptionBar = ref()
const { height } = useElementSize(chatOptionBar)

const messageListStyle = computed(() => {
  return {
    height: `calc(100% - ${height.value}px)`
  }
})
</script>

<style scoped>
.chat_bar {
  position: relative;
  background-color: var(--color-background-pro);
  height: 100%;
  width: 80%;
}

.chat_title {
  position: fixed;
  width: 100%;
  background-color: var(--color-background-soft);
}

.message_list {
  width: 100%;
  height: calc(100% - 3rem);
  overflow: auto;
}

.message_list::-webkit-scrollbar {
  width: 0.5rem;
  height: 0.5rem;
}

.message_list::-webkit-scrollbar-thumb {
  border-radius: 1rem;
}

.message_list:hover::-webkit-scrollbar-thumb {
  background: #ccc;
}

.message_content {
  display: flex;
  flex-direction: column;
}

.time_stamp {
  display: inline;
  position: sticky;
  font-size: 0.75rem;
  top: 0.25rem;
  background-color: var(--color-background-soft);
  border-radius: 1.25rem;
  border: 1px solid var(--color-background-pro);
  padding: 0.5rem 1rem;
  margin: 0.25rem auto;
  z-index: 1;
}
</style>
