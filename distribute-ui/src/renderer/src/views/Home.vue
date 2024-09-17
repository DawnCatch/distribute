<template>
  <div class="home">
    <SideBar />
    <Split :weight="0.125" />
    <ChatBar />
    <VideoCall />
  </div>
</template>

<script setup lang="ts">
import { setToken } from '@renderer/utils/secure'

import SideBar from '../components/side/SideBar.vue'
import ChatBar from '../components/chat/ChatBar.vue'
import VideoCall from '../components/chat/VideoCall.vue'
import Split from '@renderer/components/Split.vue'
import mitt from '@renderer/utils/mitt'

mitt.on('reload', () => {
  setToken('')
  location.reload()
})
</script>

<style scoped>
.home {
  position: relative;
  display: flex;
  height: 100%;
}
</style>
