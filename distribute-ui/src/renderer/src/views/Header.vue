<template>
  <div data-tauri-drag-region class="title_bar">
    <div v-if="appStore.checkItem.id !== -1" class="chat_title">
      {{ appStore.checkItem.title }}
    </div>
    <div class="window_tool">
      <div class="option" @click.left="windowMinimize">
        <Icon name="minimize" custom-class="option_icon" />
      </div>
      <div class="option" @click.left="windowMaximize">
        <Icon :name="maxState ? 'enlarge' : 'narrow'" custom-class="option_icon" />
      </div>
      <div class="option highlight" @click.left="windowClose">
        <Icon name="close" custom-class="option_icon" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAppStore } from '../stores/appStore'

import Icon from '../components/Icon.vue'

const appStore = useAppStore()

const maxState = ref(false)

const ipcRenderer = window.electron.ipcRenderer

async function windowMinimize() {
  ipcRenderer.send('window-min')
}

function windowMaximize() {
  maxState.value = !maxState.value
  ipcRenderer.send('window-max')
}

function windowClose() {
  ipcRenderer.send('window-close')
}
</script>

<style scoped>
.title_bar {
  -webkit-app-region: drag;
  user-select: none;
  display: flex;
  background-color: var(--color-background-mute);
}

.chat_title {
  line-height: 1.5rem;
  margin-left: 0.5rem;
}

.window_tool {
  -webkit-app-region: no-drag;
  display: flex;
  margin-left: auto;
}

.option {
  display: flex;
  color: var(--color-edit);
  padding: 0.25rem 1rem;
  justify-content: center;
}

.option:hover {
  color: var(--color-text);
  background-color: var(--color-background-soft);
}

.highlight:hover {
  background-color: var(--color-warning);
}
</style>

<style>
.option_icon {
  width: 1rem;
  height: 1rem;
}
</style>
