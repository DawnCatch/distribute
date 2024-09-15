<template>
  <div v-if="appStore.isLogin" class="chat_bar">
    <div class="tool_bar">
      <div class="chat_text_box">
        <div class="title_text">{{ appStore.currentTitle }}</div>
        <div class="detail_text">0 人</div>
      </div>
      <div class="option_box">
        <Icon name="search" custom-class="icon" />
        <Icon name="split" custom-class="icon" />
        <Icon name="more" custom-class="icon" />
      </div>
    </div>
    <Split direction :weight="0.125" />
    <ScrollBox class="message_list" :style="messageListStyle">
      <div v-for="(item, index) in group" :key="index" class="message_content">
        <div class="time_stamp">
          {{ time(item) }}
        </div>
        <Message
          v-for="(messages, key) in item as Record<number, MessageModel[]>"
          :key
          :messages="messages"
        />
      </div>
    </ScrollBox>
    <ChatoptionBar ref="chatOptionBar" />
  </div>
</template>

<script setup lang="ts">
import { Message as MessageModel, useAppStore } from '../../stores/appStore'
import { computed, ref } from 'vue'
import mitt from '../../utils/mitt'
import { useElementSize } from '@vueuse/core'
import { getTime } from '@renderer/utils/utils'

import Message from './Message.vue'
import ChatoptionBar from './ChatOptionBar.vue'
import Icon from '../Icon.vue'
import Split from '../Split.vue'
import ScrollBox from '../ScrollBox.vue'

const appStore = useAppStore()

const group = computed(() => {
  try {
    const result = appStore.currentGroup
    console.log(result)
    return result
  } catch (e) {
    return {}
  }
})

// eslint-disable-next-line @typescript-eslint/no-unused-vars
function send() {
  mitt.emit('rtc:request', appStore.ownId)
}

function time(item: Record<number, MessageModel[]>) {
  for (const key in item) {
    return getTime(item[key][0].date)
  }
  return ''
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
/*
临界30rem
*/
.chat_bar {
  position: relative;
  background-color: var(--color-background-pro);
  height: 100%;
  width: 0;
  flex: 1;
  border-left: 5px var(--color-background-soft);
  display: flex;
  flex-direction: column;
}

.chat_title {
  position: fixed;
  width: 100%;
  background-color: var(--color-background-soft);
}

.message_list {
  width: 100%;
  height: 0;
  /* height: calc(100% - 3rem); */
  overflow: auto;
  flex: 1;
}

/* .message_list::-webkit-scrollbar {
  width: 0.5rem;
  height: 0.5rem;
}

.message_list::-webkit-scrollbar-thumb {
  border-radius: 1rem;
}

.message_list:hover::-webkit-scrollbar-thumb {
  background: #ccc;
} */

.message_content {
  display: flex;
  flex-direction: column;
}

.tool_bar {
  display: flex;
  padding: 0.5rem;
}

.chat_text_box {
  margin-left: 0.5rem;
}

.option_box {
  display: flex;
  margin-left: auto;
  align-items: center;
}

.icon {
  width: 1.25rem;
  height: 1.25rem;
  cursor: pointer;
  margin-left: 1rem;
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
