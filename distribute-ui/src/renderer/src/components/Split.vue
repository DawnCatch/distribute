<template>
  <div class="line" :style></div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  direction: {
    type: Boolean,
    default: false
  },
  weight: {
    type: Number,
    required: true
  },
  rem: {
    type: Boolean,
    default: false
  },
  px: {
    type: Boolean,
    default: false
  }
})

const style = computed(() => {
  const result = {} as Style
  let suffix = 'rem'
  if (props.rem) {
    suffix = 'rem'
  } else if (props.px) {
    suffix = 'px'
  }
  const weight = props.weight + suffix
  if (props.direction) {
    result.width = '100%'
    result.height = weight
  } else {
    result.height = '100%'
    result.width = weight
  }
  return { ...result }
})
</script>

<script lang="ts">
interface Style {
  width: string
  height: string
}
</script>

<style scoped>
.line {
  background-color: var(--color-background-mute);
}
</style>
