<template>
  <div>
    <img src="../assets/avatar.jpg" :class="type ? 'group' : 'user'" alt="avatar" />
  </div>
</template>

<script setup lang="ts">
defineProps({
  type: {
    type: Boolean,
    default: false
  }
})
</script>

<style scoped>
img {
  width: 100%;
}

.user {
  border-radius: 50%;
}

.group {
  border-radius: 25%;
}
</style>
