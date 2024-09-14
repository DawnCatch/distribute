<template>
  <div class="rtc_content">
    {{ from }}{{ appStore.profile.userId }}
    <div v-if="content.type === 'RTC:REQ'">
      <template v-if="from !== appStore.profile.userId">
        <div @click="agree">同意</div>
        <div @click="refuse">拒绝</div>
      </template>
      <template v-else> 你发起一条视频电话 </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAppStore } from '../../stores/appStore'
import mitt from '../../utils/mitt'

const appStore = useAppStore()

const props = defineProps({
  content: {
    type: Object,
    required: true
  },
  from: {
    type: Number,
    required: true
  }
})

function agree() {
  mitt.emit('rtc:callback', {
    userId: props.from,
    status: true,
    session: JSON.parse(props.content.value)
  })
}

function refuse() {
  mitt.emit('rtc:callback', { userId: props.from, status: false })
}
</script>

<style scoped></style>
