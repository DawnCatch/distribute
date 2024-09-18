<template>
  <div v-element-visibility="onElementVisibility" class="content_bar" :class="type">
    <TextContent v-if="type === 'text'" :content="message.content" />
    <ImageContent v-else-if="type === 'image'" :content="message.content" />
    <span v-if="spaceVisible" class="space"></span>
    <span class="time">{{ getTime(message.date) }}</span>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { vElementVisibility } from '@vueuse/components'

import { getTime } from '@renderer/utils/utils'

import ImageContent from './ImageContent.vue'
import TextContent from './TextContent.vue'
import { http } from '@renderer/utils/http'
import { useAppStore } from '@renderer/stores/appStore'

const props = defineProps({
  message: {
    type: Object,
    required: true
  },
  reverse: {
    type: Boolean,
    default: false
  }
})

const appStore = useAppStore()

const type = computed(() => {
  const { type, value } = props.message.content as Content
  if (type === 'TEXT') return 'text'
  else if (type === 'FILE') {
    const files = value.split(',')
    for (let i = 0; i < files.length; i++) {
      const suffix = files[i].split('.')[1]
      if (
        ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'tiff'].includes(suffix.toLowerCase())
      )
        return 'image'
    }
    return 'file'
  }
  return ''
})

function onElementVisibility(state: boolean) {
  if (props.reverse) return
  if (state && !props.message.observers.includes(appStore.own.userId)) {
    http({
      url: '/message/read',
      data: {
        id: props.message.id
      }
    })
  }
}

const spaceVisible = computed(() => {
  if (type.value === 'image') {
    return false
  } else return true
})
</script>

<style scoped>
.content_bar {
  display: flex;
  position: relative;
  padding: 0.5rem 0.5rem;
  margin: 0.1rem 0;
  word-wrap: break-word;
  border-radius: 0.5rem;
  overflow: hidden;
  background-color: var(--color-background-mute);
}

.image {
  padding: 0;
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
