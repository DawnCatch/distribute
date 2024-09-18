<template>
  <Dialog
    class="sign_dialog"
    mask
    :visible="visible"
    transition="sign"
    custom-class="sign_dialog_content"
    @click-mask-listen="close"
  >
    <ReverseSwitchPage v-model="active" :page-num="2" class="sign_box">
      <template #page="{ data }">
        <div v-if="data.index === 0">
          <h1>登录</h1>
          <div class="input_box login_box">
            <EditText v-model="user.username" placeholder="用户名" />
            <EditText v-model="user.password" type="password" placeholder="密码" />
          </div>
          <Button ref="loginButton" @click="login"> 登录 </Button>
          <div class="space"></div>
          <div class="tips">
            没有账号?
            <div class="tips_button" @click="data.select(1)">去注册</div>
          </div>
        </div>
        <div v-else-if="data.index === 1">
          <h1>注册</h1>
          <div class="input_box login_box">
            <EditText v-model="user.username" placeholder="用户名" />
            <EditText v-model="user.password" type="password" placeholder="密码" />
          </div>
          <Button ref="registerButton" @click="register"> 注册 </Button>
          <div class="space"></div>
          <div class="tips">
            已有账号?
            <div class="tips_button" @click="data.select(0)">去登录</div>
          </div>
        </div>
      </template>
    </ReverseSwitchPage>
  </Dialog>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, reactive, ref } from 'vue'

import { useAppStore } from '../../stores/appStore'

import Dialog from '../Dialog.vue'
import EditText from '../EditText.vue'
import Button from '../Button.vue'
import ReverseSwitchPage from '../ReverseSwitchPage.vue'

import { http } from '../../utils/http'
import mitt from '../../utils/mitt'

onMounted(() => {
  mitt.on('SignDialog:open', open)
  mitt.on('SignDialog:close', close)
})

onUnmounted(() => {
  mitt.off('SignDialog:open')
  mitt.off('SignDialog:close')
})

const appStore = useAppStore()

const visible = ref(false)
const user: User = reactive({ username: '', password: '' } as User)

const loginButton = ref<InstanceType<typeof Button> | null>()
const registerButton = ref<InstanceType<typeof Button> | null>()

function login() {
  loginButton.value?.wait()
  http({
    method: 'POST',
    url: '/user/login',
    data: user
  }).then((res) => {
    if (res.status) {
      appStore.setOwn(res.data as UserProfile)
      close()
    }
    loginButton.value?.release()
  })
}

function register() {
  registerButton.value?.wait()
  http({
    method: 'POST',
    url: '/user/register',
    data: user
  }).then((res) => {
    if (res.status) {
      appStore.setOwn(res.data as UserProfile)
      close()
    }
    registerButton.value?.release()
  })
}

function open() {
  visible.value = true
}

function close() {
  visible.value = false
  user.username = ''
  user.password = ''
}

const active = ref(0)
</script>

<script lang="ts">
interface User {
  username: string
  password: string
}
</script>

<style scoped>
.sign_dialog {
  z-index: 2;
}

.sign_box {
  margin: auto;
  width: 19rem;
  height: 26rem;
  border-radius: 2rem;
  background-color: var(--color-background-soft);
  padding: 2rem;
}

.title {
  position: relative;
  top: -1rem;
  width: 100%;
  margin: auto;
}

.title > h1,
.title > p {
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

.space {
  height: 1rem;
}

.tips {
  display: flex;
  justify-content: center;
}

.tips_button {
  border-bottom: 1px solid black;
  cursor: pointer;
}
</style>

<style>
.sign_dialog_content {
  margin: auto;
}

.sign-enter-active {
  animation: sign_slide_in 0.1s linear;
}

.sign-leave-active {
  animation: sign_slide_in 0.1s linear reverse;
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
