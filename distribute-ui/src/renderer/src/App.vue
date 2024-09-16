<script setup lang="ts">
import { onMounted, watch } from 'vue'

import { UserProfile, useAppStore, Message, Union } from './stores/appStore'
import { RouterView } from 'vue-router'
import Header from './views/Header.vue'
import { http } from './utils/http'
import socket from './utils/socket'
import mitt from './utils/mitt'
import notification from './utils/notification'

const appStore = useAppStore()

onMounted(() => {
  http({
    method: 'POST',
    url: '/user/reconnect'
  }).then((res) => {
    if (res.status) {
      appStore.setOwn(res.data as UserProfile)
    }
  })
})
watch(
  () => appStore.isLogin,
  (newVal) => {
    if (!newVal) return
    http({
      method: 'POST',
      url: '/message/history',
      data: {
        from: '0',
        to: ''
      }
    }).then((res) => {
      if (res.status) {
        appStore.addMessages(res.data as Message[])
      }
    })
    http({
      method: 'POST',
      url: '/relation/list/union'
    }).then((res) => {
      if (res.status) {
        appStore.setUnion(res.data as Union)
      }
    })
    socket()
    mitt.on('on-message', (value) => {
      const message = value as Message
      appStore.addMessage(message as Message)
      if (message.from !== appStore.ownId) {
        notification({
          title: `收到一条来自${appStore.relations.filter((it) => it.id === message.from && it.type === message.type)[0].title}的消息`,
          body: message.content.value
        })
      }
    })
  },
  {
    immediate: false,
    deep: true
  }
)
</script>

<template>
  <div class="container">
    <Header />
    <div class="router_box">
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.router_box {
  height: calc(100% - 1.5rem);
}
</style>
