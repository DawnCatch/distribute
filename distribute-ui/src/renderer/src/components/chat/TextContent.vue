<template>
  <div v-dompurify-html="html" class="content"></div>
</template>

<script setup lang="ts">
import markdown from '@renderer/utils/markdown';
import { computed } from 'vue';

const props = defineProps({
  content: {
    type: Object,
    required: true
  }
})

const html = computed(() => markdown.render(props.content.value))
</script>

<style scoped>
.content {
  width: 100%;
}
</style>
