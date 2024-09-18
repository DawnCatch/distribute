<template>
  <div class="message_bar" :class="{ reverse: reverse }">
    <div v-if="!reverse" class="avatar_box">
      <img src="../../assets/avatar.jpg" alt="avatar" />
    </div>
    <div class="message_box">
      <AutoContent
        v-for="(message, index) in messages as Message[]"
        :key="index"
        :message
        :reverse
      />
    </div>
    <Avatar v-if="reverse" class="avatar_box" :src="appStore.own.avatarUrl" />
  </div>
</template>

<script setup lang="ts">
import { useAppStore, Message } from '../../stores/appStore'

import { computed } from 'vue'

import AutoContent from './AutoContent.vue'
import Avatar from '../Avatar.vue'

const appStore = useAppStore()

const props = defineProps({
  messages: {
    type: Array,
    required: true
  }
})

const reverse = computed(() => {
  const { type, id } = appStore.current
  const { from, to } = props.messages[0] as Message
  if (type) {
    return from === appStore.own.userId
  } else {
    return to === id
  }
})
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
</style>
