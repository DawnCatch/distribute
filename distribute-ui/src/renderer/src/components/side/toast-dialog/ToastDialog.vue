<template>
  <ScrollDialog class="toast_dialog" title="消息" :visible="visible" @click-mask-listen="close">
    <template #content>
      <div class="pending_item_list">
        <ToastDialogItem v-for="id in relationStore.pends" :id :key="id" />
        <div v-if="relationStore.pends.length === 0" class="tip_box">暂无待处理消息</div>
      </div>
    </template>
  </ScrollDialog>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'

import ScrollDialog from '../../ScrollDialog.vue'
import ToastDialogItem from './ToastDialogItem.vue'

import mitt from '@renderer/utils/mitt'
import { useRelationStore } from '@renderer/stores/relationStore'

const visible = ref(false)

onMounted(() => {
  mitt.on('ToastDialog:open', open)
  mitt.on('ToastDialog:close', close)
})

onUnmounted(() => {
  mitt.off('ToastDialog:open')
  mitt.off('ToastDialog:close')
})

function open() {
  visible.value = true
}

function close() {
  visible.value = false
}

const relationStore = useRelationStore()
</script>

<style scoped>
.toast_dialog {
  z-index: 2;
}

.pending_item_list {
  padding: 0 1rem;
}

.tip_box {
  width: 100%;
  text-align: center;
}
</style>
