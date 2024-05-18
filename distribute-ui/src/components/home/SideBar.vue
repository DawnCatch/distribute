<template>
    <div class="sider_bar">
        <div class="top_bar">
            <div class="avatar_box" :class="{ avatar_navigation: navigationDialogVisible,avatar_sign: signDialogVisible }">
                <img src="../../assets/avatar.jpg" alt="avatar" @click="openNavigation"
                    v-if="appStore.profile.nickname">
                <Icon name="avatar" customClass="avatar_default" @click="openSignDialog" v-else />
            </div>
            <div>搜索</div>
            <div>添加</div>
        </div>
        <div class="session_list"></div>
        <NavigationDialog />
        <SignDialog />
    </div>
</template>

<script setup lang="ts">
import { ComponentInternalInstance, getCurrentInstance, onMounted, onUnmounted, ref } from "vue";

import { useAppStore } from "../../stores/appStore"

import Icon from "../Icon.vue"

import NavigationDialog from "./NavigationDialog.vue"
import SignDialog from "./SignDialog.vue";

const { proxy } = getCurrentInstance() as ComponentInternalInstance

onMounted(() => {
    proxy?.$mitt.on("NavigationDialog:visible", (visible) => {
        navigationDialogVisible.value = visible as boolean
    })
    proxy?.$mitt.on("SignDialog:visible", (visible) => {
        signDialogVisible.value = visible as boolean
    })
})

onUnmounted(() => {
    proxy?.$mitt.off("NavigationDialog:visible")
    proxy?.$mitt.off("SignDialog:visible")
})

const appStore = useAppStore()

const navigationDialogVisible = ref(false)
const signDialogVisible = ref(false)

function openNavigation() {
    proxy?.$mitt.emit('NavigationDialog:open')
}

function openSignDialog() {
    proxy?.$mitt.emit('SignDialog:open')
}
</script>

<style scoped>
.sider_bar {
    height: 100%;
    width: 30%;
    background-color: var(--color-background-soft)
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
    z-index: 2;
}

.avatar_navigation {
    scale: 2;
    top: 2rem;
    left: 2rem;
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