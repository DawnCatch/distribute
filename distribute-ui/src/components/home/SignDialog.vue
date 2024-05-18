<template>
    <Dialog mask :visible="visible" @clickMaskListen="close" customClass="sign_box" transition="sign">
        <h1>登录</h1>
        <div class="input_box login_box">
            <EditText v-model="user.username" placeholder="用户名" />
            <EditText type="password" v-model="user.password" placeholder="密码" />
        </div>
        <Button @click="login" ref="loginButton">
            登录
        </Button>
    </Dialog>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, onUnmounted, reactive, ref, watch } from "vue";
import type { ComponentInternalInstance } from 'vue'

import { Profile, useAppStore } from "../../stores/appStore";
import { HttpResponse } from "../../utils/http";

import Dialog from "../Dialog.vue";
import EditText from "../EditText.vue"
import Button from "../Button.vue";

const { proxy } = getCurrentInstance() as ComponentInternalInstance

onMounted(() => {
    proxy?.$mitt.on("SignDialog:open", open)
    proxy?.$mitt.on("SignDialog:close", close)
})

onUnmounted(() => {
    proxy?.$mitt.off("SignDialog:open")
    proxy?.$mitt.off("SignDialog:close")
})

const appStore = useAppStore()

const visible = ref(false)
const user: User = reactive({ username: "", password: "" } as User)

const loginButton = ref<InstanceType<typeof Button> | null>()

function login() {
    loginButton.value?.wait()
    proxy?.$http({
        method: "POST",
        url: "/user/login",
        data: user,
        callback: (res) => {
            if (res.status) {
                appStore.setProfile(res.data as Profile)
                close()
            }
            loginButton.value?.release()
        }
    })
}

function open() {
    visible.value = true
}

function close() {
    visible.value = false
}

watch(visible, (newValue) => {
    console.log(newValue)
    proxy?.$mitt.emit("SignDialog:visible", newValue)
}, {
    immediate: false,
    deep: false,
})
</script>

<script lang="ts">
interface User {
    username: string,
    password: string
}


</script>

<style scoped>
.title {
    position: relative;
    top: -1rem;
    width: 100%;
    margin: auto;
}

.title>h1,
.title>p {
    margin: 3rem;
}

h1 {
    margin: 1rem;
}

p {
    margin: 1rem;
}

.input_box {
    display: flex;
    flex-wrap: wrap;
    height: 60%;
    gap: 2rem;
}

.login_box {
    padding: 15% 0;
}
</style>

<style>
.sign_box {
    margin: auto;
    width: 25%;
    height: 26rem;
    max-height: 80%;
    border-radius: 2rem;
    background-color: var(--color-background-soft);
    padding: 2rem;
}

.sign-enter-active {
    animation: sign_slide_in .1s linear;
}

.sign-leave-active {
    animation: sign_slide_in .1s linear reverse;
}

@keyframes sign_slide_in {
    from {
        transform: translateX(10%);
    }

    to {
        transform: translateX(0px);
    }
}
</style>