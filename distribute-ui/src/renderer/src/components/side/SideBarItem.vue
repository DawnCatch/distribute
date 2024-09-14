<template>
  <div class="profile_box" @click="select">
    {{ item.title }}
  </div>
</template>

<script setup lang="ts">
import { useAppStore } from '../../stores/appStore'

const appStore = useAppStore()

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

function select() {
  appStore.check(props.item)
}
</script>
