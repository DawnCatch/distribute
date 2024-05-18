<template>
    <Dialog mask :visible="visible" @clickMaskListen="close" customClass="option_box" transition="navigation">
        <div class="user_box">
            {{ appStore.profile.nickname }}
        </div>
    </Dialog>
</template>

<script setup lang="ts">
import { ComponentInternalInstance, getCurrentInstance, onMounted, onUnmounted, ref, watch } from 'vue';

import { useAppStore } from '../../stores/appStore';

import Dialog from '../Dialog.vue'

const appStore = useAppStore()

const { proxy } = getCurrentInstance() as ComponentInternalInstance

onMounted(() => {
    proxy?.$mitt.on("NavigationDialog:open", open)
    proxy?.$mitt.on("NavigationDialog:close", close)
})

onUnmounted(() => {
    proxy?.$mitt.off("NavigationDialog:open")
    proxy?.$mitt.off("NavigationDialog:close")
})



const visible = ref(false)

function open() {
    visible.value = true
}

function close() {
    visible.value = false
}

watch(visible, (newValue) => {
    console.log(newValue)
    proxy?.$mitt.emit("NavigationDialog:visible", newValue)
}, {
    immediate: false,
    deep: false,
})

</script>

<style scoped>
.user_box {
    padding-left: 8rem;
}
</style>

<style>
.option_box {
    height: 100%;
    width: 25%;
    background-color: rgb(32, 36, 36);
    box-shadow: 0 0 .5rem black;
    overflow: hidden;
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
</style>