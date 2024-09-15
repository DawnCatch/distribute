<template>
  <div class="side_bar">
    <div class="top_bar">
      <div class="avatar_box" :class="{ avatar_navigation: navigationDialogVisible, avatar_sign: signDialogVisible }">
        <img v-if="appStore.profile.nickname" src="../../assets/avatar.jpg" alt="avatar" @click="openNavigation" />
        <Icon v-else name="avatar" custom-class="avatar_default" @click="openSignDialog" />
      </div>
      <div>添加</div>
    </div>
    <div ref="searchBoxRef" class="search_input_box">
      <BorderEditText ref="searchRef" v-model="text" placeholder="搜索" />
    </div>
    <div class="search_content" :class="{ visible: searchFocus }">
      <SearchBox />
    </div>
    <ScrollBox class="session_list" :class="{ visible: !searchFocus }">
      <SideBarItem v-for="(item, index) in appStore.relations" :key="index" :item="item" />
    </ScrollBox>
    <NavigationDialog />
    <SignDialog />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'

import { useAppStore } from '../../stores/appStore'

import Icon from '../Icon.vue'
import SideBarItem from './SideBarItem.vue'
import BorderEditText from '../BorderEditText.vue'
import SearchBox from './SearchBox.vue'
import ScrollBox from '../ScrollBox.vue'

import NavigationDialog from './NavigationDialog.vue'
import SignDialog from './SignDialog.vue'
import mitt from '../../utils/mitt'
import { useFocusWithin } from '@vueuse/core'

const text = ref('')

onMounted(() => {
  mitt.on('NavigationDialog:visible', (visible) => {
    navigationDialogVisible.value = visible as boolean
  })
  mitt.on('SignDialog:visible', (visible) => {
    signDialogVisible.value = visible as boolean
  })
})

onUnmounted(() => {
  mitt.off('NavigationDialog:visible')
  mitt.off('SignDialog:visible')
})

const appStore = useAppStore()

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
}

.avatar_box {
  display: flex;
  position: relative;
  top: 0;
  left: 0;
  height: 3rem;
  width: 3rem;
  background-color: aquamarine;
  border-radius: 50%;
  overflow: hidden;
  transition: all 0.5s;
  z-index: 3;
}

.avatar_navigation {
  scale: 1.5;
  top: 1rem;
  left: 1rem;
}

.avatar_sign {
  z-index: 1;
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
