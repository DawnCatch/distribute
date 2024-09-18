<template>
  <Dialog mask :visible="visible" transition="sign"
    :custom-class="dialogContentState.scroll ? 'scroll_dialog_full' : 'scroll_dialog'">
    <div class="dialog_content" :style="dialogContentStyle">
      <div ref="topBarRef" class="top_bar">
        <div class="tool_bar" :class="{ default_tool_bar: title !== '' }">
          <div class="title_box">
            <div v-if="title !== ''" class="title">{{ title }}</div>
            <slot v-else name="title"></slot>
          </div>
          <div class="option_bar">
            <slot name="option"></slot>
          </div>
        </div>
        <div class="other">
          <slot name="other"></slot>
        </div>
      </div>
      <ScrollBox class="scroll_box" :scroll="handleScroll" fold>
        <div ref="scrollContentRef" class="scroll_content">
          <slot name="content"></slot>
        </div>
      </ScrollBox>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { computed, nextTick, reactive, ref, watch } from 'vue'
import { useElementSize, useWindowSize } from '@vueuse/core'

import Dialog from './Dialog.vue'
import ScrollBox from './ScrollBox.vue'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  visible: {
    type: Boolean,
    required: true
  }
})

const windowSize = useWindowSize()

const scrollContentRef = ref<HTMLElement | null>()
const { height: scrollContentHeight } = useElementSize(scrollContentRef)

const topBarRef = ref<HTMLElement | null>()

const headerHeight = 1.5 * 16
const screenPadding = 5 * 16

const workArea = computed(() => {
  const { width, height } = windowSize
  return {
    width: width.value,
    height: height.value - headerHeight
  }
})

const dialogContentState = reactive({
  scroll: false,
  height: 0,
  bottomBorder: true
})
const dialogContentStyle = computed(() => {
  const borderRadiusWeight = 12
  const borderRadiusStyle = {
    borderTopLeftRadius: `${borderRadiusWeight}px`,
    borderTopRightRadius: `${borderRadiusWeight}px`,
    borderBottomLeftRadius: `${borderRadiusWeight}px`,
    borderBottomRightRadius: `${borderRadiusWeight}px`
  }
  if (!dialogContentState.bottomBorder) {
    borderRadiusStyle.borderBottomLeftRadius = '0'
    borderRadiusStyle.borderBottomRightRadius = '0'
  }
  return {
    height: `${dialogContentState.height}px`,
    marginTop: dialogContentState.scroll ? `${screenPadding}px` : '',
    ...borderRadiusStyle,
    paddingBottom: dialogContentState.bottomBorder ? '' : '0'
  }
})

let oldScrollTop = 0
function handleScroll(element) {
  const { scrollTop, clientHeight } = element
  const { height } = workArea.value
  const diff = scrollContentHeight.value - screenPadding - (clientHeight + scrollTop)
  if (scrollTop > oldScrollTop && diff < 0) {
    dialogContentState.height -= scrollTop - oldScrollTop
    dialogContentState.bottomBorder = true
    if (dialogContentState.height < height - screenPadding * 2) {
      dialogContentState.height = height - screenPadding * 2
    }
  } else if (scrollTop < oldScrollTop && dialogContentState.bottomBorder) {
    dialogContentState.height -= scrollTop - oldScrollTop
    if (dialogContentState.height > height - screenPadding) {
      dialogContentState.height = height - screenPadding
      dialogContentState.bottomBorder = false
    }
  }
  oldScrollTop = scrollTop
}

function caculation() {
  nextTick(() => {
    if (!topBarRef.value || !scrollContentRef.value) return
    const topBarHeight = topBarRef.value.clientHeight
    const scrollContentHeight = scrollContentRef.value.clientHeight
    const { height } = workArea.value
    if (scrollContentHeight + topBarHeight > height - screenPadding) {
      dialogContentState.scroll = true
      dialogContentState.height = height - screenPadding
      dialogContentState.bottomBorder = false
    } else {
      dialogContentState.scroll = false
      dialogContentState.height = topBarHeight + scrollContentHeight + 2 * 0.5 * 16
      dialogContentState.bottomBorder = true
    }
  })
}

watch(
  () => props.visible,
  (newVal) => {
    if (!newVal) return
    caculation()
  },
  { immediate: true }
)
watch(scrollContentHeight, () => {
  caculation()
})
</script>

<style scoped>
.dialog_content {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 22rem;
  background-color: var(--color-background);
  padding-bottom: 0.5rem;
}

.default_tool_bar {
  padding: 1rem 1rem;
}

.tool_bar {
  display: flex;
}

.title {
  font-weight: bolder;
}

.option_bar {
  margin-left: auto;
}

.space {
  height: 1rem;
}

.scroll_box {
  width: 100%;
  flex: 1;
}
</style>

<style>
.scroll_dialog_full {
  margin: 0 auto;
}

.scroll_dialog {
  margin: auto;
}

.dialog_scroll_icon {
  width: 1rem;
  height: 1rem;
}
</style>
