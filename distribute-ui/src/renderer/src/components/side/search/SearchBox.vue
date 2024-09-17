<template>
  <div class="search_box" tabindex="0">
    <TabBar v-model="active" :list>
      <template #title="{ title }">
        <div class="tab_item">{{ title.title }}</div>
      </template>
    </TabBar>
    <TabView v-model="active" class="tab_view" :list>
      <template #page="{ data }">
        <div v-if="data.index === 0">page1</div>
        <div v-else-if="data.index === 1">page2</div>
        <div v-else-if="data.index === 2">page3</div>
      </template>
    </TabView>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import TabBar from '../../tab/TabBar.vue'
import TabView from '../../tab/TabView.vue'

const list = ref([
  {
    title: '对话'
  },
  {
    title: '频道'
  },
  {
    title: '消息'
  }
])

const active = ref(0)
</script>

<style scoped>
.search_box {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.tab_item {
  font-weight: bolder;
}

.tab_view {
  flex: 1;
}
</style>
