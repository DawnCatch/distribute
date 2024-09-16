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
      <div v-for="(item, index) in searchResult.filter((it) => it.type === type)" :key="index">
        {{ item }}
        <Avatar />
      </div>
    </template>
  </ScrollDialog>
</template>

<script setup lang="ts">
import ScrollDialog from '@renderer/components/ScrollDialog.vue'
import TabBar from '@renderer/components/tab/TabBar.vue'
import BorderEditText from '@renderer/components/BorderEditText.vue'

import mitt from '@renderer/utils/mitt'
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { watchDebounced } from '@vueuse/core'
import { http } from '@renderer/utils/http'
import Avatar from '@renderer/components/Avatar.vue'

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

<script lang="ts">
interface SearchResultItem {
  type: boolean
  id: number
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

.list-enter-active,
.list-leave-active {
  transition: all 0.1s;
}

.list-enter,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>

<style>
.add_dialog_icon {
  height: 1.25rem;
  width: 1.25rem;
}
</style>
