<template>
  <div ref="searchItemRef" class="search_item">
    <Avatar class="avatar" :type="item.type" />
    <div class="title">{{ relation?.title ?? 'None' }}</div>
    <div class="option_box" @click="option.work(item.id)">
      <div>{{ option.title }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watchEffect } from 'vue'

import Avatar from '@renderer/components/Avatar.vue'

import { useRelationStore } from '@renderer/stores/relationStore'
import { application, follow } from '@renderer/utils/http'
import { useElementVisibility } from '@vueuse/core'

const relationStore = useRelationStore()

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const searchItemRef = ref<HTMLElement | null>()
const visibled = useElementVisibility(searchItemRef)

const relation = computed(() => {
  const { type, id } = props.item
  return relationStore.relation(type, id)
})

watchEffect(() => {
  if (relation.value !== undefined || !visibled) return
  const { type, id } = props.item
  relationStore.getRelation(type, id)
})

const link = computed(() => {
  const { type, id } = props.item
  return relationStore.link(type, id)
})

const options = {
  '=': [
    {
      title: '取消关注',
      work: follow
    },
    {
      title: '退出群组',
      work: (id: number) => { }
    }
  ],
  '>': [
    {
      title: '取消关注',
      work: follow
    },
    {
      title: '取消申请',
      work: application
    }
  ],
  '<': [
    {
      title: '回关',
      work: follow
    },
    {
      title: '同意邀请',
      work: (id: number) => { }
    }
  ],
  '!': [
    {
      title: '关注',
      work: follow
    },
    {
      title: '发送申请',
      work: application
    }
  ]
} as Record<string, Option[]>

const option = computed(() => {
  const { type } = props.item
  return options[link.value][type ? 1 : 0]
})
</script>

<style scoped>
.search_item {
  display: flex;
  align-items: center;
  height: 4rem;
}

.avatar {
  height: 3rem;
  width: 3rem;
}

.title {
  margin-left: 1rem;
}

.option_box {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: auto;
  margin-right: 1rem;
  width: 5rem;
  height: 2rem;
  border-radius: 0.5rem;
  background-color: var(--color-background-soft);
  cursor: pointer;
}

.option_box:hover {
  background-color: var(--color-background-mute);
}
</style>
