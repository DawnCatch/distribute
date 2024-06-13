<script setup lang="ts">
import { onMounted, ref, watch, } from 'vue';

import { Message, Profile, Relation, useAppStore } from './stores/appStore';

import { RouterView } from "vue-router"
import Header from "./views/Header.vue"
import { http } from './utils/http';
import { socket } from './utils/socket';
import { notification } from './utils/notification';
import { Options } from '@tauri-apps/api/notification';
import TauriWebSocket from "tauri-plugin-websocket-api";
import { platform as platformEnv } from './utils/env';

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
  http({
    method: "POST",
    url: "/message/history",
    data: {
      from: "2024-05-18 00:00:00",
      to: ""
    }
  }).then((res) => {
    if (res.status) {
      appStore.setMessage(res.data as Message[])
    }
  })
  http({
    method: "GET",
    url: "/relation/list/union"
  }).then((res) => {
    if (res.status) {
      appStore.setFollows(res.data.follows as Relation[])
      appStore.setFans(res.data.fans as Relation[])
      appStore.setGroup(res.data.groups as Relation[])
      appStore.setApplications(res.data.applications as Relation[])
    }
  })
  socket({
    url: "/chat",
    onOpen: (_: TauriWebSocket | WebSocket) => {
      console.log("打开链接")
    },
    onMessage: (message: Message) => {
      appStore.addMessage(message)
      // notification({
      //   title: `收到一条来自${appStore.profiles.filter((it) => it.userId === message.from)[0].nickname}的消息`,
      //   body: message.content.value
      // } as Options);
    },
    onClose: () => console.log("链接断开")
  })
}, {
  immediate: false,
  deep: true,
})

const platform = ref(platformEnv)
</script>

<template>
  <div class="container">
    <Header v-if="platform" />
    <div :class="{ tauri: platform, web: !platform }">
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

.tauri {
  height: calc(100% - 1.5rem);
}

.web {
  height: 100%;
}
</style>
