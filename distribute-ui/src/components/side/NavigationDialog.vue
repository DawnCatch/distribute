<template>
    <Dialog mask :visible="visible" @clickMaskListen="close" customClass="navigation_box" transition="navigation">
        <div class="user_box box" @click="extend">
            <div class="user_detail">
                <div class="detail_text">
                    {{ appStore.profile.nickname }}
                </div>
                <div class="detail_text click_text">
                    设置状态
                </div>
            </div>
            <div class="btn_extend" :class="{ 'reverse': deviceListVisible }">
                <Icon name="down" customClass="btn_extend_icon" />
            </div>
        </div>
        <div class="device_list" :class="{ 'device_list_visible': deviceListVisible }">

        </div>
        <div class="option_box box">
            <div @click="signOut">退出登录</div>
        </div>
    </Dialog>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, watch } from 'vue';

import { useAppStore } from '../../stores/appStore';

import Dialog from '../Dialog.vue'
import Icon from "../Icon.vue"
import mitt from '../../utils/mitt';
import { setToken } from '../../utils/secure';

const appStore = useAppStore()

onMounted(() => {
    mitt.on("NavigationDialog:open", open)
    mitt.on("NavigationDialog:close", close)
})

onUnmounted(() => {
    mitt.off("NavigationDialog:open")
    mitt.off("NavigationDialog:close")
})



const visible = ref(false)

function open() {
    visible.value = true
}

function close() {
    visible.value = false
}

const deviceListVisible = ref(false)

function extend() {
    deviceListVisible.value = !deviceListVisible.value
}

watch(visible, (newValue) => {
    mitt.emit("NavigationDialog:visible", newValue)
}, {
    immediate: false,
    deep: false,
})

function signOut() {
    setToken("")
}

</script>

<style scoped>
.box {
    width: 100%;
    padding: 1rem 1rem;
}

.user_box {
    margin-top: 6rem;
    padding: 0 1rem 1rem 1rem;
    display: flex;
    border-bottom: 1px solid var(--color-border-hover);
    cursor: pointer;
}

.click_text {
    color: var(--color-edit-focus);
}

.btn_extend {
    display: flex;
    height: 2rem;
    width: 2rem;
    margin: auto 0 auto auto;
    color: var(--color-edit);
    transition: all .25s;
}

.reverse {
    transform: rotate(180deg);
}

.device_list {
    padding: 0rem 2rem;
    height: 0%;
    transition: all .25s;
}

.device_list_visible {
    padding: .25rem 2rem;
}

.option_box {
    display: flex;
    border-top: 1px solid var(--color-border-hover);
}
</style>

<style>
.navigation_box {
    height: 100%;
    width: 18%;
    background-color: var(--color-background-soft);
    box-shadow: 0 0 .5rem black;
    overflow-y: hidden;
}

.navigation-enter-active {
    animation: navigation_slide_in .5s;
}

.navigation-leave-active {
    animation: navigation_slide_in .5s reverse;
}

@keyframes navigation_slide_in {
    from {
        transform: translateX(-100%);
    }

    to {
        transform: translateX(0px);
    }
}

.btn_extend_icon {
    height: 2rem;
    width: 2rem;
}
</style>