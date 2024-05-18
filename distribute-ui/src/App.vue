<script setup lang="ts">
import { RouterView } from "vue-router"
import Header from "./views/Header.vue"
</script>

<template>
  <div class="container">
    <Header />
    <RouterView />
  </div>
</template>

<style scoped>
.container {
  margin: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>
