<template>
  <div class="message_content">
    <div v-for="image in images" :key="image">
      <img :src="image" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { buildUrl } from '@renderer/utils/http';
import { computed } from 'vue';

const props = defineProps({
  content: {
    type: Object,
    required: true
  }
})

const images = computed(() => {
  const { value } = props.content as Content
  const files = value.split(',').map((it) => buildUrl(`/file/${it}`))
  return files
})
</script>

<style scoped>
img {
  max-width: 10rem;
  max-height: 10rem;
}

.message_content {
  width: 100%;
}
</style>
