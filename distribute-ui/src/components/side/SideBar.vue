<template>
    <div class="side_bar">
        <div class="top_bar">
            <div class="avatar_box"
                :class="{ avatar_navigation: navigationDialogVisible, avatar_sign: signDialogVisible }">
                <img src="../../assets/avatar.jpg" alt="avatar" @click="openNavigation"
                    v-if="appStore.profile.nickname">
                <Icon name="avatar" customClass="avatar_default" @click="openSignDialog" v-else />
            </div>
            <div>添加</div>
        </div>
        <div class="search_box">搜索</div>
        <div class="session_list">
            <SideBarItem v-for="(item, index) in appStore.relations" :profile="item" :index />
        </div>
        <NavigationDialog />
        <SignDialog />
    </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue";

import { useAppStore } from "../../stores/appStore"

import Icon from "../Icon.vue"
import SideBarItem from "./SideBarItem.vue"

import NavigationDialog from "./NavigationDialog.vue"
import SignDialog from "./SignDialog.vue";
import mitt from "../../utils/mitt";

onMounted(() => {
    mitt.on("NavigationDialog:visible", (visible) => {
        navigationDialogVisible.value = visible as boolean
    })
    mitt.on("SignDialog:visible", (visible) => {
        signDialogVisible.value = visible as boolean
    })
})

onUnmounted(() => {
    mitt.off("NavigationDialog:visible")
    mitt.off("SignDialog:visible")
})

const appStore = useAppStore()

const navigationDialogVisible = ref(false)
const signDialogVisible = ref(false)

function openNavigation() {
    mitt.emit('NavigationDialog:open')
}

function openSignDialog() {
    mitt.emit('SignDialog:open')
}
</script>

<style scoped>
.side_bar {
    height: 100%;
    width: 20%;
    background-color: var(--color-background-soft);
    border-right: 1px solid var(--color-background-pro);
    /* resize: horizontal; */
    /* overflow: hidden; */
}

.top_bar {
    padding: .5rem;
    display: flex;
}

.avatar_box {
    display: flex;
    position: relative;
    top: 0;
    left: 0;
    height: 3rem;
    width: 3rem;
    background-color: aquamarine;
    border-radius: 50%;
    overflow: hidden;
    transition: all .5s;
    z-index: 3;
}

.avatar_navigation {
    scale: 1.5;
    top: 1rem;
    left: 1rem;
}

.avatar_sign {
    z-index: 1;
}

.avatar_default {
    height: 75%;
    width: 75%;
    margin: 12.5%;
}
</style>