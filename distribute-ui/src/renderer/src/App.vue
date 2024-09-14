<script setup lang="ts">
import { onMounted, watch } from 'vue'

import { Profile, useAppStore, Message } from './stores/appStore'
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
      appStore.setProfile(res.data as Profile)
    }
  })
})
watch(
  () => appStore.profile,
  () => {
    const messages = appStore.messages
    let before = 0
    for (let i = 0; i < messages.length; i++) {
      const it = messages[i]
      if (before < it.date) {
        before = it.date
      }
    }
    http({
      method: 'POST',
      url: '/message/history',
      data: {
        from: `${before}`,
        to: ''
      }
    }).then((res) => {
      if (res.status) {
        appStore.setMessage(res.data as Message[])
      }
    })
    http({
      method: 'POST',
      url: '/relation/list/union'
    }).then((res) => {
      if (res.status) {
        appStore.setUnion(res.data as Profile[])
        console.log(appStore.relations)
      }
    })
    socket()
    mitt.on('on-message', (message: Message) => {
      appStore.addMessage(message)
      if (message.from !== appStore.profile.userId) {
        notification({
          title: `收到一条来自${appStore.relations.filter((it) => it.id === message.from && it.type === message.type)[0].title}的消息`,
          body: message.content.value
        } as Options)
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
