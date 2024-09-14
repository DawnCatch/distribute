<template>
  <div class="search_box" tabindex="0">
    <TabView v-model="active" :list>
      <template #title="{ title }">
        <div class="tab_block">{{ title.title }}</div>
      </template>
    </TabView>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import TabView from '../TabView.vue'

const list = ref([
  {
    title: 'hello',
    info: 0
  },
  {
    title: 'fucky',
    info: 0
  }
])

const active = ref(0)
</script>

<style scoped>
.search_box {
  width: 100%;
  height: 100%;
}
</style>
