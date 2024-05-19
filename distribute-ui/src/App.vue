<script setup lang="ts">
import { onMounted, watch, } from 'vue';

import { Profile, useAppStore } from './stores/appStore';

import { RouterView } from "vue-router"
import Header from "./views/Header.vue"
import { http } from './utils/http';
import { socket } from './utils/socket';
import { Message } from 'tauri-plugin-websocket-api';

const appStore = useAppStore()


onMounted(() => {
  http({
    method: "POST",
    url: "/user/reconnect",
  }).then((res) => {
    if (res.status) {
      appStore.setProfile(res.data as Profile)
    }
  })
})

watch(() => appStore.profile, () => {
  socket({
    url: "/chat",
    onOpen: () => {
      console.log("打开链接")
    },
    onMessage: (message: Message) => {
      appStore.addMessage(message)
      console.log(message)
    },
    onClose: () => console.log("链接断开")
  })
}, {
  immediate: false,
  deep: true,
})
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
