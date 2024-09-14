<template>
  <div class="tab_view">
    <div class="tab_list">
      <div v-for="(item, index) in list as Item[]" ref="tabItemRef" :key="index" class="tab_item">
        <slot :title="item" name="title"></slot>
      </div>
    </div>
    <div class="line"></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps({
  list: {
    type: Array,
    default: [] as Item[]
  },
  active: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['update:active'])

function select(index: number) {
  emit('update:active', index)
}

const tabItemRef = ref<HTMLElement[] | null>()

const currentIndex = ref(0)

watch(
  () => props.active,
  () => {

  },
  {
    immediate: false,
    deep: true
  }
)
</script>

<script lang="ts">
interface Item {
  title: string
}
</script>

<style scoped>
.tab_list {
  display: flex;
  overflow-x: auto;
}

.line {
  height: 0.125rem;
  background-color: aqua;
  border-radius: 0.1rem 0.1rem 0 0;
}
</style>
