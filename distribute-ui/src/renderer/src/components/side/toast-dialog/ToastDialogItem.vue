<template>
  <div ref="toastDialogItemRef" class="toast_dialog_item">
    <div class="avatar_box">
      <Avatar class="avatar" :src="profile?.avatarUrl" />
      {{ profile?.title ?? 'None' }}
    </div>
    <div ref="detailBoxRef" class="detail_box" :class="detailBoxClass" @click="handle">
      <div v-if="isHover && mode">同意</div>
      <div v-else-if="isHover && !mode">拒绝</div>
      <div v-else>申请加入</div>
    </div>
    <div class="avatar_box">
      <Avatar class="avatar" type :src="relation?.avatarUrl" />
      <div class="title_text">{{ relation?.title ?? 'None' }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useElementHover, useElementVisibility } from '@vueuse/core'
import { computed, ref, watch } from 'vue'

import Avatar from '@renderer/components/Avatar.vue'

import { useRelationStore } from '@renderer/stores/relationStore'
import { http } from '@renderer/utils/http'

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

const relationStore = useRelationStore()

const relation = computed(() => {
  return relationStore.relation(true, props.id)
})

const profile = computed(() => {
  console.log(relation.value)
  if (!relation.value) return undefined
  const temp = relationStore.relationByTarget(false, relation.value.userId)
  if (!temp) relationStore.getRelationByTarget(false, relation.value.userId)
  return temp
})

const toastDialogItemRef = ref<HTMLElement | null>()
const isVisibled = useElementVisibility(toastDialogItemRef)

watch(
  isVisibled,
  (newVal) => {
    if (relation.value || !newVal) return
    relationStore.getRelation(true, props.id)
  },
  { immediate: true }
)

const detailBoxRef = ref<HTMLElement | null>()
const isHover = useElementHover(detailBoxRef)
const mode = ref(true)

watch(isHover, (newVal) => {
  if (!newVal) mode.value = !mode.value
})

function handle() {
  if (!relation.value) return
  const id = relation.value.id
  http({
    method: 'POST',
    url: '/relation/handle',
    data: {
      id: id,
      status: mode.value
    }
  }).then((res) => {
    if (!res.status) return
    relationStore.removePend(id)
  })
}

const detailBoxClass = computed(() => {
  if (isHover.value && !mode.value) return 'warning'
  else return ''
})
</script>

<style scoped>
.toast_dialog_item {
  display: flex;
  align-items: center;
}

.avatar_box {
  display: flex;
  align-items: center;
  width: 7.5rem;
}

.title_text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  padding-right: 1rem;
}

.avatar {
  width: 3rem;
  height: 3rem;
}

.detail_box {
  text-align: center;
  flex: 1;
  margin: 0 0.25rem;
  background-color: var(--color-background-mute);
  padding: 0.25rem;
  border-radius: 0.5rem;
  cursor: pointer;
}

.warning {
  background-color: rgb(85, 35, 35);
  color: white;
}
</style>
