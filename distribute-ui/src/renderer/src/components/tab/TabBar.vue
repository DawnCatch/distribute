<template>
  <div ref="tabBarRef" class="tab_bar">
    <div ref="tabListRef" class="tab_list" @scroll="handleScroll">
      <div
        v-for="(item, index) in list as Item[]"
        :key="index"
        class="tab_item"
        :class="{ flex: flex }"
        @click="select(index)"
      >
        <div ref="tabItemRef">
          <slot :title="item" name="title"></slot>
        </div>
      </div>
    </div>
    <div class="line" :style="lineStyle"></div>
  </div>
</template>

<script setup lang="ts">
import { useElementVisibility, useScroll } from '@vueuse/core'
import { computed, onMounted, reactive, ref, watch } from 'vue'

const props = defineProps({
  list: {
    type: Array,
    default: [] as Item[]
  },
  modelValue: {
    type: Number,
    required: true
  },
  duration: {
    type: Number,
    default: 0.25
  },
  flex: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

function select(index: number) {
  emit('update:modelValue', index)
}

const tabBarRef = ref<HTMLElement | null>()
const visible = useElementVisibility(tabBarRef)

watch(visible, (newVal) => {
  if (!newVal) return
  setLine()
})

const currentIndex = ref(0)

watch(
  () => props.modelValue,
  (newVal) => {
    currentIndex.value = newVal
    setLine()
  }
)

onMounted(() => {
  handleScroll()
  setLine()
})

const tabItemRef = ref<HTMLElement[] | null>()
function getTabItemElement(index: number): ElementData {
  if (!tabItemRef.value)
    return {
      width: 0,
      offsetLeft: 0
    }
  const tabItemElement = tabItemRef.value[index]
  const tabItemWidth = tabItemElement.offsetWidth
  const tabItemOffsetLeft = tabItemElement.offsetLeft
  return {
    width: tabItemWidth,
    offsetLeft: tabItemOffsetLeft
  }
}

const line = reactive({ width: 0, offsetLeft: 0, deviation: 0 } as Line)
function setLine() {
  const { width, offsetLeft } = getTabItemElement(currentIndex.value)
  line.width = width
  line.offsetLeft = offsetLeft
  {
    if (!tabListRef.value) return
    const { clientWidth, scrollLeft, offsetLeft } = tabListRef.value
    const containStart = scrollLeft + offsetLeft
    const containEnd = containStart + clientWidth
    const lineStart = line.offsetLeft - line.deviation
    const lineEnd = lineStart + line.width
    if (lineStart < containStart) {
      x.value = line.offsetLeft - line.width
    } else if (lineEnd > containEnd) {
      x.value = line.offsetLeft - line.width
    }
  }
}

const lineStyle = computed(() => {
  return {
    width: `${line.width}px`,
    transform: `translateX(${line.offsetLeft - line.deviation}px)`,
    transitionDuration: `${props.duration}s`
  }
})

const tabListRef = ref<HTMLElement | null>()
const { x } = useScroll(tabListRef, { behavior: 'smooth' })
function handleScroll() {
  if (!tabListRef.value) return
  const element = tabListRef.value
  const { scrollLeft } = element
  line.deviation = scrollLeft
}
</script>

<style scoped>
.tab_bar {
  overflow: hidden;
  cursor: pointer;
}

.tab_list {
  display: flex;
  overflow-x: auto;
}

.tab_list::-webkit-scrollbar {
  height: 0;
}

.tab_item {
  padding: 0.5rem 0.5rem 0.25rem 0.5rem;
}

.flex {
  flex: 1;
  text-align: center;
}

.line {
  height: 0.25rem;
  background-color: var(--color-tab-bar-line);
  border-radius: 4px 4px 0 0;
}
</style>
