<template>
  <div ref="pageSwicherRef" class="page_swicher">
    <div v-for="index in pageNum" :key="index" ref="pageRef" class="page">
      <div v-if="currentIndex === index - 1">
        <slot :data="{ index: index - 1, select }" name="page"></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAnimate } from '@vueuse/core'
import { ref, watch } from 'vue'

const props = defineProps({
  pageNum: {
    type: Number,
    required: true
  },
  modelValue: {
    type: Number,
    required: true
  }
})

const duration = 500

const emit = defineEmits(['update:modelValue'])

const currentIndex = ref(props.modelValue)

function select(index: number) {
  emit('update:modelValue', index)
  setTimeout(() => {
    currentIndex.value = index
  }, duration / 2)
}

const pageSwicherRef = ref<HTMLElement | null>()
const { play } = useAnimate(
  pageSwicherRef,
  [{ transform: 'rotateY(0deg)' }, { transform: 'rotateY(90deg)' }, { transform: 'rotateY(0deg)' }],
  duration
)

watch(
  () => props.modelValue,
  () => {
    play()
  }
)

const pageRef = ref<HTMLElement[] | null>()
</script>