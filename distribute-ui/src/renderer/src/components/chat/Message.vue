<template>
  <div class="message_bar" :class="{ reverse: reverse }">
    <div v-if="!reverse" class="avatar_box">
      <img v-if="appStore.profile.nickname" src="../../assets/avatar.jpg" alt="avatar" />
      <Icon v-else name="avatar" custom-class="avatar_default" />
    </div>
    <div class="message_box">
      <div
        v-for="(message, index) in messages as Message[]"
        :key="index"
        v-element-visibility="(state: boolean) => onElementVisibility(state, message)"
        class="content_bar"
      >
        <div
          v-if="message.content.type === 'TEXT'"
          v-dompurify-html="md2Ele(message.content)"
          class="content"
        ></div>
        <span class="space"></span>
        <span class="time">{{ getTime(message.date) }}</span>
        <!-- {{ message.observers }} -->
      </div>
    </div>
    <div v-if="reverse" class="avatar_box">
      <img v-if="appStore.profile.nickname" src="../../assets/avatar.jpg" alt="avatar" />
      <Icon v-else name="avatar" custom-class="avatar_default" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAppStore, Content, Message } from '../../stores/appStore'
import markdown from '../../utils/markdown'
import { vElementVisibility } from '@vueuse/components'

import Icon from '../Icon.vue'
import { computed } from 'vue'
import { http } from '../../utils/http'

const appStore = useAppStore()

const props = defineProps({
  messages: {
    type: Array,
    required: true
  }
})

const reverse = computed(() => {
  const { type, id } = appStore.checkItem
  const { from, to } = props.messages[0] as Message
  if (type) {
    return from === appStore.profile.userId
  } else {
    return to === id
  }
})

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

function md2Ele(item: Content) {
  return markdown.render(item.value)
}

function onElementVisibility(state: boolean, message: Message) {
  if (reverse.value) return
  if (state && !message.observers.includes(appStore.profile.userId)) {
    http({
      url: '/message/read',
      data: {
        id: message.id
      }
    })
  }
}
</script>

<style scoped>
.message_bar {
  display: flex;
  padding: 0.25rem 1rem 1rem 1rem;
  align-items: flex-end;
}

.reverse {
  justify-content: flex-end;
}

.avatar_box {
  display: flex;
  position: sticky;
  bottom: 0.5rem;
  height: 3rem;
  width: 3rem;
  margin-bottom: 0.3rem;
  background-color: aquamarine;
  border-radius: 50%;
  overflow: hidden;
}

.message_box {
  display: flex;
  flex-direction: column;
  max-width: calc(100% - 8rem);
  padding: 0 1rem;
}

.content_bar {
  display: flex;
  position: relative;
  padding: 1rem 1rem;
  margin: 0.1rem 0;
  word-wrap: break-word;
  border-radius: 1rem;
  background-color: var(--color-background-mute);
}

.content {
  width: 100%;
}

.space {
  display: inherit;
  width: 1.5rem;
}

.time {
  position: absolute;
  right: 0.5rem;
  bottom: 0.5rem;
  font-size: 0.5rem;
  color: var(--color-edit);
}
</style>
