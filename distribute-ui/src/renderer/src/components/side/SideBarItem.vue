<template>
  <div class="profile_box" :class="{ focus: isFocus }" @click="select">
    <Avatar class="avatar" :src="relation?.avatarUrl" :type />
    <div class="space"></div>
    <div class="detail_box">
      <div class="top_box">
        <div class="title">
          {{ relation?.nickname }}
        </div>
        <div class="date">{{ date }}</div>
      </div>
      <div class="message_box">
        <div class="message_text">{{ text }}</div>
        <div v-if="newsLen" class="message_len">{{ newsLen }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Avatar from '@renderer/components/Avatar.vue'

import { Message, Relation, useAppStore } from '../../stores/appStore'
import { useRelationStore } from '../../stores/relationStore'

import { computed, ref, watch } from 'vue'
import { getTime } from '@renderer/utils/utils'

const appStore = useAppStore()
const relationStore = useRelationStore()

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const relation = computed(() => {
  const { type, id } = props.item
  return relationStore.relation(type, id)
})

watch(
  relation,
  (newVal) => {
    if (newVal !== undefined) return
    const { type, id } = props.item
    relationStore.getRelation(type, id)
  },
  { immediate: true }
)

const type = computed(() => {
  return (props.item as Relation).type
})

const isFocus = computed(() => {
  const { type, id } = appStore.current
  return props.item.type === type && props.item.id === id
})

function select() {
  const { type, id } = props.item
  appStore.setCurrent(type, id)
}

const map = computed(() => {
  try {
    const result = appStore.messageMap
    return result
  } catch (e) {
    return []
  }
})

const text = ref('')
const newsLen = ref(0)
const date = ref('')

watch(
  map,
  (newVal) => {
    let messages: Message[]
    try {
      messages = newVal[props.item.type ? '1' : '0'][props.item.id] ?? []
    } catch (e) {
      return
    }
    const ownId = appStore.own.userId
    let index = 0
    let len = 0
    if (messages.length === 0) return
    for (let i = 0; i < messages.length; i++) {
      const { date } = messages[i]
      if (date > messages[index].date) {
        index = i
      }
    }
    for (let i = messages.length - 1; i >= 0; i--) {
      const { from, observers } = messages[i]
      if (from === ownId || observers.includes(ownId)) break
      len++
    }
    const message = messages[index]
    text.value = ''
    if (message.from !== ownId) {
      text.value = '=> '
    }
    date.value = getTime(message.date)
    if (message.content.type === 'FILE') text.value += '[文件]'
    else text.value += message.content.value
    newsLen.value = len
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<style scoped>
* {
  user-select: none;
}

.profile_box {
  display: flex;
  width: 100%;
  padding: 0.5rem;
  cursor: pointer;
}

.profile_box:hover {
  background-color: var(--color-side-bar-item-background-hover);
}

.focus,
.focus:hover {
  background-color: var(--color-side-bar-item-background-focus);
}

.avatar {
  display: flex;
  width: 3.5rem;
  height: 3.5rem;
  overflow: hidden;
}

.user {
  border-radius: 50%;
}

.group {
  border-radius: 25%;
}

.space {
  width: 5%;
}

.detail_box {
  flex: 1;
}

.top_box,
.message_box {
  height: 50%;
  width: 100%;
  display: flex;
  align-items: center;
}

.title {
  font-size: larger;
}

.date,
.message_len {
  margin-left: auto;
}

.date {
  font-size: small;
}

.message_text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 0;
  flex: 1;
}

.message_len {
  padding: 0.25rem;
  border-radius: 0.75rem;
  background-color: var(--color-toast-tip-background-mute);
  color: var(--color-side-bar-item-background-hover);
  font-size: 0.75rem;
  display: flex;
  justify-content: center;
  width: 1.25rem;
  height: 1.25rem;
}

.focus .message_len {
  background-color: var(--color-toast-tip-background-soft);
  color: var(--color-side-bar-item-background-focus);
}
</style>
