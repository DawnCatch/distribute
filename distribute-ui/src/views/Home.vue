<template>
    <div class="home">
        <SideBar />
        <ChatBar />
        <VideoCall />
    </div>
</template>

<script setup lang="ts">
import SideBar from "../components/side/SideBar.vue"
import ChatBar from "../components/chat/ChatBar.vue";
import VideoCall from "../components/chat/VideoCall.vue";
</script>

<style scoped>
.home {
    position: relative;
    display: flex;
    height: 100%
}
</style>