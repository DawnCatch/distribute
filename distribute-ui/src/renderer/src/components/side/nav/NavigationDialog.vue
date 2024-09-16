<template>
  <Dialog
    mask
    :visible="visible"
    custom-class="navigation_box"
    transition="navigation"
    @click-mask-listen="close"
  >
    <div class="user_box box" @click="extend">
      <div class="user_detail">
        <div class="detail_text">
          {{ appStore.own.nickname }}
        </div>
        <div class="detail_text click_text">设置状态</div>
      </div>
      <div class="btn_extend" :class="{ reverse: deviceListVisible }">
        <Icon name="down" custom-class="btn_extend_icon" />
      </div>
    </div>
    <div class="device_list" :class="{ device_list_visible: deviceListVisible }"></div>
    <div class="option_list">
      <NavigationOptionItem title="添&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp加" @click="openAddDialog">
        <Icon name="invite" custom-class="navigation_icon" />
      </NavigationOptionItem>
      <NavigationOptionItem title="收&nbsp&nbsp藏&nbsp&nbsp夹">
        <Icon name="mark" custom-class="navigation_icon" />
      </NavigationOptionItem>
      <NavigationOptionItem
        title="夜间模式"
        :default-value="isDark"
        type="switch"
        :use="changeTheme"
      >
        <Icon name="night" custom-class="navigation_icon" />
      </NavigationOptionItem>
      <NavigationOptionItem title="设置">
        <Icon name="setting" custom-class="navigation_icon" />
      </NavigationOptionItem>
      <NavigationOptionItem title="退出登录" :use="signOut">
        <Icon name="exit" custom-class="navigation_icon" />
      </NavigationOptionItem>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, watch } from 'vue'

import { useAppStore } from '../../../stores/appStore'

import Dialog from '../../Dialog.vue'
import Icon from '../../Icon.vue'
import NavigationOptionItem from './NavigationOptionItem.vue'

import mitt from '../../../utils/mitt'
import { useDark, useToggle } from '@vueuse/core'

const appStore = useAppStore()

onMounted(() => {
  mitt.on('NavigationDialog:open', open)
  mitt.on('NavigationDialog:close', close)
})

onUnmounted(() => {
  mitt.off('NavigationDialog:open')
  mitt.off('NavigationDialog:close')
})

const visible = ref(false)

function open() {
  visible.value = true
}

function close() {
  visible.value = false
}

const deviceListVisible = ref(false)

function extend() {
  deviceListVisible.value = !deviceListVisible.value
}

watch(
  visible,
  (newValue) => {
    mitt.emit('NavigationDialog:visible', newValue)
  },
  {
    immediate: false,
    deep: false
  }
)

function signOut() {
  mitt.emit('reload')
  close()
}

const isDark = useDark()
const toggleDark = useToggle(isDark)

function changeTheme() {
  setTimeout(() => toggleDark(), 150)
  return true
}

function openAddDialog() {
  close()
  mitt.emit('AddDialog:open', true)
}
</script>

<style scoped>
.box {
  width: 100%;
  padding: 1rem 1rem;
}

.user_box {
  margin-top: 6rem;
  padding: 0 1rem 1rem 1rem;
  display: flex;
  border-bottom: 1px solid var(--color-border-hover);
  cursor: pointer;
}

.click_text {
  color: var(--color-edit-focus);
}

.btn_extend {
  display: flex;
  height: 2rem;
  width: 2rem;
  margin: auto 0 auto auto;
  color: var(--color-edit);
  transition: all 0.25s;
}

.reverse {
  transform: rotate(180deg);
}

.device_list {
  padding: 0rem 2rem;
  height: 0%;
  transition: all 0.25s;
}

.device_list_visible {
  padding: 0.25rem 2rem;
}
</style>

<style>
.navigation_box {
  height: 100%;
  width: 16rem;
  background-color: var(--color-background-soft);
  box-shadow: 0 0 0.5rem black;
  overflow-y: hidden;
}

.navigation-enter-active {
  animation: navigation_slide_in 0.5s;
}

.navigation-leave-active {
  animation: navigation_slide_in 0.5s reverse;
}

@keyframes navigation_slide_in {
  from {
    transform: translateX(-100%);
  }

  to {
    transform: translateX(0px);
  }
}

.btn_extend_icon {
  height: 2rem;
  width: 2rem;
}

.navigation_icon {
  width: 1.5rem;
  height: 1.5rem;
}
</style>
