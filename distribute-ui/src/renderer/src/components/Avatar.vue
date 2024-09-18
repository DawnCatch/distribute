<template>
  <div class="avatar" :class="type ? 'group' : 'user'" alt="avatar" @dragover="fileDragover">
    <img :src="img" />
    <label v-if="edit" class="shadow" @drop="fileDrop" @dragleave="fileDragleave">
      <Icon name="camera" custom-class="avatar_icon" />
      <input id="upload" type="file" accept="image/*,.pdf" @change="handleFileChange" />
    </label>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import Icon from './Icon.vue'
import { buildUrl } from '@renderer/utils/http'

const props = defineProps({
  src: {
    type: String,
    default: ''
  },
  type: {
    type: Boolean,
    default: false
  },
  edit: {
    type: Boolean,
    default: false
  }
})

const img = computed(() => {
  const src = props.src
  if (/^blob:http/.test(src)) return src
  if (src === '') {
    return new URL('@renderer/assets/avatar.jpg', import.meta.url).href
  } else {
    return buildUrl(`/file/${src}`)
  }
})

const shadowVisible = ref(false)

function fileDragover(e) {
  e.preventDefault()
  shadowVisible.value = true
}

function fileDragleave(e: DragEvent) {
  e.preventDefault()
  if (!e.target) return
  const target = e.target as HTMLElement
  if (target.className === 'shadow') {
    shadowVisible.value = false
  }
}

const emit = defineEmits(['check-file'])

function fileDrop(e: DragEvent) {
  e.preventDefault()
  shadowVisible.value = false
  if (!e.dataTransfer) return
  const file = e.dataTransfer.files[0]
  emit('check-file', file)
}

function handleFileChange(e: Event) {
  if (!e.target) return
  const target = e.target as HTMLInputElement
  if (!target.files) return
  const file = target.files[0]
  emit('check-file', file)
}
</script>

<style scoped>
.avatar {
  overflow: hidden;
  position: relative;
}

img {
  width: 100%;
  background-repeat: repeat;
}

.user {
  border-radius: 100%;
}

.group {
  border-radius: 25%;
}

.shadow {
  display: v-bind("shadowVisible ? 'flex' : 'none'");
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 0;
  cursor: pointer;
  border-radius: 100%;
}

#upload {
  display: none;
}

.avatar:hover .shadow {
  display: flex;
}
</style>

<style>
.avatar_icon {
  height: 3rem;
  width: 3rem;
  color: var(--color-background);
}
</style>
