<template>
  <div class="side_bar">
    <div class="top_bar">
      <div
        class="avatar_box"
        :class="{ avatar_navigation: navigationDialogVisible, avatar_sign: signDialogVisible }"
      >
        <img
          v-if="appStore.profile.nickname"
          src="../../assets/avatar.jpg"
          alt="avatar"
          @click="openNavigation"
        />
        <Icon v-else name="avatar" custom-class="avatar_default" @click="openSignDialog" />
      </div>
      <div>添加</div>
    </div>
    <div ref="searchBoxRef" class="search_input_box">
      <BorderEditText ref="searchRef" v-model="text" placeholder="搜索" />
    </div>
    <div v-if="searchFocus" class="search_content">
      <SearchBox />
    </div>
    <div v-else ref="scrollable" class="session_list">
      <SideBarItem v-for="(item, index) in appStore.relations" :key="index" :item="item" />
    </div>
    <NavigationDialog />
    <SignDialog />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'

import { useAppStore } from '../../stores/appStore'

import Icon from '../Icon.vue'
import SideBarItem from './SideBarItem.vue'
import BorderEditText from '../BorderEditText.vue'
import SearchBox from './SearchBox.vue'

import NavigationDialog from './NavigationDialog.vue'
import SignDialog from './SignDialog.vue'
import mitt from '../../utils/mitt'
import { useElementHover, useFocusWithin } from '@vueuse/core'

const text = ref('')

onMounted(() => {
  if (scrollable.value) {
    scrollable.value.addEventListener('scroll', handleScroll)
  }
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

const scrollable = ref<HTMLElement | null>(null)
const scrollVisible = ref(false)
const scrollTop = ref('0px')
const scrollThumTop = ref('0px')
const scrollThumHeight = ref('0px')
const isHovered = useElementHover(scrollable)
watch(
  isHovered,
  () => {
    handleScroll()
  },
  {
    immediate: false,
    deep: false
  }
)
function handleScroll() {
  if (scrollable.value) {
    const scrollableElement = scrollable.value
    if (scrollableElement.scrollHeight > scrollableElement.clientHeight) {
      scrollVisible.value = true
    }
    const heightRatio = scrollableElement.clientHeight / scrollableElement.scrollHeight
    const top =
      (scrollableElement.scrollTop /
        (scrollableElement.scrollHeight - scrollableElement.clientHeight)) *
      (scrollableElement.clientHeight * (1 - heightRatio))
    scrollThumHeight.value = heightRatio * 100 + '%'
    scrollTop.value = scrollableElement.scrollTop + 'px'
    scrollThumTop.value = scrollableElement.scrollTop + top + 'px'
  }
}

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
.side_bar {
  height: 100%;
  width: 20%;
  background-color: var(--color-background-soft);
  border-right: 1px solid var(--color-background-pro);
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
  flex: 1;
}

.search_box {
  width: 100%;
  height: 100%;
}

.session_list {
  overflow-y: auto;
  flex: 1;
  position: relative;
  display: flex;
  flex-direction: column;
}

.session_list::after {
  display: none;
  content: '';
  background-color: var(--color-scrollbar-thumb-background);
  width: 0.25rem;
  height: v-bind('scrollThumHeight');
  position: absolute;
  top: v-bind('scrollThumTop');
  right: 0;
  margin: 0.1rem;
  border-radius: 0.25rem;
}

.session_list::before {
  display: none;
  content: '';
  background-color: var(--color-scrollbar-background);
  width: 0.25rem;
  height: 100%;
  position: absolute;
  top: v-bind('scrollTop');
  right: 0;
  margin: 0.1rem;
  border-radius: 0.25rem;
}

.session_list:hover::before,
.session_list:hover::after {
  display: v-bind("scrollVisible ? 'flex' : 'none'");
}

.session_list::-webkit-scrollbar {
  width: 0;
}
</style>
