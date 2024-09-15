<template>
  <div class="profile_box" :class="{ focus: isFocus }" @click="select">
    <Avatar class="avatar" :class="type ? 'group' : 'user'" />
    <div class="space"></div>
    <div class="detail_box">
      <div class="top_box">
        <div class="title">
          {{ item.title }}
        </div>
        <div class="date">xx:xx</div>
      </div>
      <div class="message_box">
        <div class="message_text">你好</div>
        <div class="message_len">100</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Avatar from '@renderer/components/Avatar.vue'

import { Relation, useAppStore } from '../../stores/appStore'
import { computed } from 'vue'

const appStore = useAppStore()

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const type = computed(() => {
  return (props.item as Relation).type
})

const isFocus = computed(() => {
  const { type, id } = appStore.checkItem
  return props.item.type === type && props.item.id === id
})

function select() {
  appStore.check(props.item as Relation)
}
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
  background-color: var(--color-slde-bar-item-background-hover);
}

.focus,
.focus:hover {
  background-color: var(--color-slde-bar-item-background-focus);
}

.avatar {
  display: flex;
  width: 3.5rem;
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

.message_len {
  padding: 0.25rem;
  border-radius: 0.75rem;
  background-color: var(--color-toast-tip-background-mute);
  color: var(--color-slde-bar-item-background-hover);
  font-size: 0.75rem;
}

.focus .message_len {
  background-color: var(--color-toast-tip-background-soft);
  color: var(--color-slde-bar-item-background-focus);
}
</style>
