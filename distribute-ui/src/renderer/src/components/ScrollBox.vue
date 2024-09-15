<template>
  <div ref="scrollBoxRef" class="scroll_box" @scroll="handleScroll">
    <slot></slot>
    <div class="scroll_content">
      <div ref="scrollBarRef" class="scroll_bar">
        <div ref="scrollThumbRef" class="scroll_thumb"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useElementHover } from '@vueuse/core'
import { ref, watch, nextTick } from 'vue'

const scrollBoxRef = ref<HTMLElement | null>()
const scrollBarRef = ref<HTMLElement | null>()
const scrollThumbRef = ref<HTMLElement | null>()

const scrollVisible = ref(false)
const scrollTop = ref(0)
const scrollThumbTop = ref(0)
const scrollThumbHeight = ref(0)
const isHovered = useElementHover(scrollBoxRef)

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
  nextTick(() => {
    if (scrollBoxRef.value && scrollBarRef.value && isHovered.value) {
      const scrollableElement = scrollBoxRef.value
      if (scrollableElement.scrollHeight > scrollableElement.clientHeight) {
        scrollVisible.value = true
      }
      const heightRatio = scrollableElement.clientHeight / scrollableElement.scrollHeight
      const top =
        (scrollableElement.scrollTop /
          (scrollableElement.scrollHeight - scrollableElement.clientHeight)) *
        (scrollBarRef.value.clientHeight * (1 - heightRatio))
      scrollTop.value = scrollableElement.scrollTop
      scrollThumbHeight.value = heightRatio * 100
      scrollThumbTop.value = top
    }
  })
}
</script>

<style scoped>
.scroll_box {
  overflow-y: auto;
  position: relative;
}

.scroll_box::-webkit-scrollbar {
  width: 0;
}

.scroll_content {
  display: v-bind("isHovered && scrollVisible ? 'flex' : 'none'");
  position: absolute;
  top: 0;
  right: 0;
  padding: 0.25rem 0.2rem;
  height: 100%;
}

.scroll_bar {
  background-color: var(--color-scrollbar-background);
  width: 0.25rem;
  height: 100%;
  position: relative;
  top: v-bind("scrollTop + 'px'");
  right: 0;
  border-radius: 0.25rem;
}

.scroll_thumb {
  background-color: var(--color-scrollbar-thumb-background);
  width: 0.25rem;
  height: v-bind("scrollThumbHeight + '%'");
  position: relative;
  top: v-bind("scrollThumbTop + 'px'");
  right: 0;
  border-radius: 0.25rem;
}
</style>
