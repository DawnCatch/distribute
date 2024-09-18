<template>
  <ScrollDialog class="add_dialog" title="添加" :visible="visible" @click-mask-listen="close">
    <template #other>
      <div class="edit_text_box">
        <BorderEditText ref="searchRef" v-model="text" placeholder="搜索" />
      </div>
      <TabBar v-model="active" :list flex>
        <template #title="{ title }">
          <div class="tab_item">{{ title.title }}</div>
        </template>
      </TabBar>
    </template>
    <template #content>
      <div class="seatch_item_list">
        <AddDialogSearchItem v-for="item in searchResult.filter((it) => it.type === type)"
          :key="`${item.type}:${item.id}`" :item />
      </div>
    </template>
  </ScrollDialog>
</template>

<script setup lang="ts">
import ScrollDialog from '@renderer/components/ScrollDialog.vue'
import TabBar from '@renderer/components/tab/TabBar.vue'
import BorderEditText from '@renderer/components/BorderEditText.vue'
import AddDialogSearchItem from './AddDialogSearchItem.vue'

import mitt from '@renderer/utils/mitt'
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { watchDebounced } from '@vueuse/core'
import { http } from '@renderer/utils/http'

const active = ref(0)
const list = [
  {
    title: '用户'
  },
  {
    title: '群组'
  }
]

const type = computed(() => (active.value === 0 ? false : true))

const text = ref('')

const visible = ref(false)

onMounted(() => {
  mitt.on('AddDialog:open', open)
  mitt.on('AddDialog:close', close)
})

onUnmounted(() => {
  mitt.off('AddDialog:open')
  mitt.off('AddDialog:close')
})

function open() {
  visible.value = true
}

function close() {
  visible.value = false
}

watchDebounced(
  text,
  (newVal) => {
    search(newVal)
  },
  { debounce: 500, maxWait: 2000 }
)

const searchResult = ref<SearchResultItem[]>([])

function search(value: string) {
  if (value === '') return
  http({
    url: '/search/all',
    data: {
      title: value
    }
  }).then((res) => {
    if (res.status) {
      searchResult.value = res.data as SearchResultItem[]
    }
  })
}
</script>

<style scoped>
.add_dialog {
  z-index: 2;
}

.edit_text_box {
  margin: 0 0.5rem;
}

.tab_item {
  flex: 1;
  text-align: center;
}

.search_item {
  display: flex;
}

.avatar {
  display: flex;
  width: 3.5rem;
  overflow: hidden;
}

.seatch_item_list {
  padding: 0 1rem;
}
</style>

<style>
.add_dialog_icon {
  height: 1.25rem;
  width: 1.25rem;
}
</style>
