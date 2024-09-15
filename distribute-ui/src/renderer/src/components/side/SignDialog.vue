<template>
  <Dialog mask :visible="visible" custom-class="sign_box" transition="sign" @click-mask-listen="close">
    <h1>登录</h1>
    <div class="input_box login_box">
      <EditText v-model="user.username" placeholder="用户名" />
      <EditText v-model="user.password" type="password" placeholder="密码" />
    </div>
    <Button ref="loginButton" @click="login"> 登录 </Button>
  </Dialog>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, reactive, ref, watch } from 'vue'

import { Profile, useAppStore } from '../../stores/appStore'

import Dialog from '../Dialog.vue'
import EditText from '../EditText.vue'
import Button from '../Button.vue'
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

function login() {
  loginButton.value?.wait()
  http({
    method: 'POST',
    url: '/user/login',
    data: user
  }).then((res) => {
    if (res.status) {
      appStore.setOwn(res.data as Profile)
      close()
    }
    loginButton.value?.release()
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

watch(
  visible,
  (newValue) => {
    mitt.emit('SignDialog:visible', newValue)
  },
  {
    immediate: false,
    deep: false
  }
)
</script>

<script lang="ts">
interface User {
  username: string
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
  width: 19rem;
  height: 26rem;
  border-radius: 2rem;
  background-color: var(--color-background-soft);
  padding: 2rem;
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
