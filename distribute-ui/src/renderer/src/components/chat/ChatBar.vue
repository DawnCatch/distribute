<template>
  <div v-if="appStore.isLogin && appStore.current.id !== -1" class="chat_bar">
    <div class="tool_bar">
      <div class="chat_text_box">
        <div class="title_text">{{ appStore.currentTitle }}</div>
        <div class="detail_text">0 人</div>
      </div>
      <div class="option_box">
        <Icon name="search" custom-class="chat_icon" />
        <Icon name="split" custom-class="chat_icon" />
        <Icon name="more" custom-class="chat_icon" />
      </div>
    </div>
    <Split direction :weight="0.125" />
    <ScrollBox ref="scrollRef" class="message_list" :style="messageListStyle">
      <div v-for="(item, index) in group" :key="index" class="message_content">
        <div class="time_stamp">
          {{ time(item) }}
        </div>
        <Message v-for="(messages, key) in item as Record<number, MessageModel[]>" :key :messages="messages" />
      </div>
    </ScrollBox>
    <ChatoptionBar ref="chatOptionBar" />
  </div>
  <div v-else-if="appStore.isLogin" class="tips_box">
    <div class="tips">选择一个对话开始聊天</div>
  </div>
  <div v-else class="tips_box">
    <div class="tips">请先登录</div>
  </div>
</template>

<script setup lang="ts">
import { Message as MessageModel, useAppStore } from '../../stores/appStore'
import { computed, nextTick, ref, watch } from 'vue'
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
  const { type, id } = appStore.current
  try {
    const result = appStore.messageGroup[type ? 1 : 0][id]
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

const scrollRef = ref<InstanceType<typeof ScrollBox> | null>()

watch(
  () => appStore.current,
  () => {
    nextTick(() => {
      scrollRef.value && scrollRef.value.scrollToBottom()
    })
  },
  { deep: true }
)
</script>

<style scoped>
/*
临界30rem
*/
.chat_bar {
  position: relative;
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
  overflow: auto;
  flex: 1;
  background-color: var(--color-chat-message-list-background);
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

.chat_icon {
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
  padding: 0.15rem 0.5rem;
  margin: 0.25rem auto;
  z-index: 1;
}

.tips_box {
  display: flex;
  height: 100%;
  width: 0;
  flex: 1;
  justify-content: center;
  align-items: center;
}

.tips {
  padding: 0.5rem 1rem;
  border-radius: 1rem;
  background-color: var(--color-background-mute);
}
</style>
