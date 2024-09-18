<template>
  <div class="side_bar">
    <div class="top_bar">
      <div class="avatar_box" :class="{ avatar_navigation: navigationDialogVisible, avatar_dialog: signDialogVisible }">
        <!-- <img
          v-if="appStore.isLogin"
          src="../../assets/avatar.jpg"
          alt="avatar"
          @click="openNavigation"
        /> -->
        <Avatar v-if="appStore.isLogin" :src="appStore.own.avatarUrl" @click="openNavigation" />
        <Icon v-else name="avatar" custom-class="avatar_default" @click="openSignDialog" />
      </div>
      <div class="toast_btn">
        <Icon name="toast-0" custom-class="side_bar_icon" />
      </div>
    </div>
    <div ref="searchBoxRef" class="search_input_box">
      <BorderEditText ref="searchRef" v-model="text" placeholder="搜索" />
    </div>
    <div class="search_content" :class="{ visible: searchFocus }">
      <SearchBox />
    </div>
    <ScrollBox class="session_list" :class="{ visible: !searchFocus }">
      <SideBarItem v-for="(item, index) in relationStore.ownRelations" :key="index" :item="item" />
    </ScrollBox>
    <NavigationDialog />
    <SignDialog />
    <AddDialog />
    <SettingDialog />
    <CreateGroupDialog />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useFocusWithin } from '@vueuse/core'

import { useAppStore } from '../../stores/appStore'
import { useRelationStore } from '@renderer/stores/relationStore'
import mitt from '../../utils/mitt'

import Icon from '../Icon.vue'
import SideBarItem from './SideBarItem.vue'
import BorderEditText from '../BorderEditText.vue'
import SearchBox from './search/SearchBox.vue'
import ScrollBox from '../ScrollBox.vue'
import NavigationDialog from './nav/NavigationDialog.vue'
import SignDialog from './SignDialog.vue'
import AddDialog from './nav/add-dialog/AddDialog.vue'
import SettingDialog from './nav/SettingDialog.vue'
import Avatar from '../Avatar.vue'
import CreateGroupDialog from './nav/CreateGroupDialog.vue'

const text = ref('')

onMounted(() => {
  mitt.on('NavigationDialog:visible', (visible) => {
    navigationDialogVisible.value = visible as boolean
  })
})

onUnmounted(() => {
  mitt.off('NavigationDialog:visible')
})

const appStore = useAppStore()
const relationStore = useRelationStore()

const navigationDialogVisible = ref(false)
const signDialogVisible = ref(false)

function openNavigation() {
  mitt.emit('NavigationDialog:open')
}

function openSignDialog() {
  mitt.emit('SignDialog:open')
}

const searchRef = ref<InstanceType<typeof BorderEditText> | null>()
const searchBoxRef = ref<HTMLElement | null>()
const { focused } = useFocusWithin(searchBoxRef)
const searchFocus = computed(() => {
  const isFocus = searchRef.value?.focused || focused.value
  searchRef.value?.focus(isFocus)
  return isFocus
})
</script>

<style scoped>
/*
临界14rem
*/
.side_bar {
  height: 100%;
  width: 14rem;
  display: flex;
  flex-direction: column;
}

.top_bar {
  padding: 0.5rem;
  display: flex;
  align-items: center;
}

.avatar_box {
  display: flex;
  position: relative;
  top: 0;
  left: 0;
  height: 3rem;
  width: 3rem;
  background-color: var(--color-background-pro);
  border-radius: 50%;
  overflow: hidden;
  transition: all 0.5s;
  cursor: pointer;
  z-index: 2;
}

.toast_btn {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: auto;
  cursor: pointer;
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
}

.toast_btn:hover {
  background-color: var(--color-background-mute);
}

.avatar_navigation {
  scale: 1.5;
  top: 1rem;
  left: 1rem;
}

.avatar_default {
  height: 75%;
  width: 75%;
  margin: 12.5%;
}

.search_input_box {
  padding: 0.25rem 0.5rem;
}

.search_content {
  transition: all 0.2s ease-in-out;
  height: 0;
}

.search_box {
  width: 100%;
  height: 100%;
}

.session_list {
  display: flex;
  flex-direction: column;
  transition: all 0.2s ease-in-out;
  height: 0;
}

.visible {
  flex: 1;
}
</style>

<style>
.side_bar_icon {
  height: 1.5rem;
  width: 1.5rem;
}
</style>
