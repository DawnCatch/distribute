<template>
  <div ref="tabViewRef" class="tab_view">
    <div v-for="(item, index) in list as Item[]" :key="index" ref="pageRef" class="page">
      <slot :data="{ item, index }" name="page"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useElementSize } from '@vueuse/core'
import { ref, watch } from 'vue'

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
  }
})

const tabViewRef = ref<HTMLElement | null>()
const { width } = useElementSize(tabViewRef)

watch(
  () => props.modelValue,
  () => {
    animation()
  }
)

const pageRef = ref<HTMLElement[] | null>()

function animation() {
  if (!pageRef.value) return
  for (let i = 0; i < pageRef.value.length; i++) {
    const element = pageRef.value[i]
    const currentIndex = element.offsetLeft / width.value
    const targetIndex = i - props.modelValue
    element.style.transform = `translateX(${(targetIndex - currentIndex) * 100}%)`
    element.style.transitionDuration = '500ms'
  }
}
</script>

<style scoped>
.tab_view {
  display: flex;
  overflow: hidden;
  width: 100%;
  height: 100%;
}

.page {
  flex: 0 0 auto;
  width: v-bind("width + 'px'");
  height: 100%;
}
</style>
