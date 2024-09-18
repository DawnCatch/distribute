<template>
  <div v-if="appStore.isLogin && appStore.current.id !== -1" class="chat_box">
    <div class="tool_bar">
      <div class="chat_text_box">
        <div class="title_text">{{ appStore.currentItem?.nickname }}</div>
        <div v-if="appStore.current.type" class="detail_text">{{ members }} 人</div>
      </div>
      <div class="option_box">
        <div class="icon_box">
          <Icon name="search" custom-class="chat_icon" />
        </div>
        <div class="icon_box">
          <Icon name="split" custom-class="chat_icon" @click="reversalChatDetail" />
        </div>
      </div>
    </div>
    <Split direction :weight="0.125" />
    <div class="chat_content">
      <div class="chat_user_area">
        <ScrollBox ref="scrollRef" class="message_list">
          <div v-for="(item, index) in group" :key="index" class="message_content">
            <div class="time_stamp">
              {{ time(item) }}
            </div>
            <Message
              v-for="(messages, key) in item as Record<number, Message[]>"
              :key
              :messages="messages"
            />
          </div>
        </ScrollBox>
        <ChatoptionBar ref="chatOptionBar" />
      </div>
      <ChatDetail v-show="chatDetailVisible" />
    </div>
  </div>
  <div v-else-if="appStore.isLogin" class="tips_box">
    <div class="tips">选择一个对话开始聊天</div>
  </div>
  <div v-else class="tips_box">
    <div class="tips">请先登录</div>
  </div>
</template>

<script setup lang="ts">
import { useAppStore } from '../../stores/appStore'
import { computed, nextTick, ref, watch } from 'vue'
import { getTime } from '@renderer/utils/utils'

import Message from './Message.vue'
import ChatoptionBar from './ChatOptionBar.vue'
import Icon from '../Icon.vue'
import Split from '../Split.vue'
import ScrollBox from '../ScrollBox.vue'
import ChatDetail from './ChatDetail.vue'

import { useRelationStore } from '@renderer/stores/relationStore'

const appStore = useAppStore()
const relationStore = useRelationStore()

const group = computed(() => {
  const { type, id } = appStore.current
  try {
    const result = appStore.messageGroup[type ? 1 : 0][id]
    return result
  } catch (e) {
    return {}
  }
})

const members = computed(() => {
  const { type } = appStore.current
  if (!type) return []
  const item = appStore.currentItem
  if (!item) return []
  const { targetId: id } = item
  const members = relationStore.members(id)
  relationStore.getMember(id)
  return members
})

// function send() {
//   mitt.emit('rtc:request', appStore.ownId)
// }

function time(item: Record<number, Message[]>) {
  for (const key in item) {
    return getTime(item[key][0].date)
  }
  return ''
}

const chatOptionBar = ref()

const scrollRef = ref<InstanceType<typeof ScrollBox> | null>()

watch(
  () => appStore.current,
  () => toBottom,
  { deep: true }
)

const chatDetailVisible = ref(false)
function reversalChatDetail() {
  chatDetailVisible.value = !chatDetailVisible.value
}

watch(group, toBottom, { deep: true })

function toBottom() {
  nextTick(() => {
    scrollRef.value && scrollRef.value.scrollToBottom()
  })
}
</script>

<style scoped>
/*
临界30rem
*/
.chat_box {
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
  overflow: auto;
  flex: 1;
  background-color: var(--color-chat-message-list-background);
}

.chat_content {
  width: 100%;
  height: 0;
  display: flex;
  flex: 1;
}

.chat_user_area {
  display: flex;
  flex-direction: column;
  width: 0;
  flex: 1;
}

.message_content {
  display: flex;
  flex-direction: column;
}

.tool_bar {
  display: flex;
  align-items: center;
  padding: 0.5rem;
  height: 3.5rem;
}

.chat_text_box {
  margin-left: 0.5rem;
}

.option_box {
  display: flex;
  margin-left: auto;
  align-items: center;
}

.icon_box {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2rem;
  height: 2rem;
  margin: 0 0.5rem;
  cursor: pointer;
  border-radius: 1rem;
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
<style>
.chat_icon {
  width: 1.25rem;
  height: 1.25rem;
}
</style>
