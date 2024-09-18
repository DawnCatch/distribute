<template>
  <ScrollDialog class="setting_dialog" title="设置" :visible="visible" @click-mask-listen="close">
    <template #content>
      <div class="content">
        <div class="top_bar">
          <Avatar class="avatar" edit :src="appStore.own.avatarUrl" @check-file="handleFile" />
          <div class="user_text">{{ appStore.own.nickname }}</div>
        </div>
        <div class="space"></div>
        <div class="option_box">
          <OptionItem title="账户">
            <Icon name="person-1" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="通知">
            <Icon name="toast-1" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="隐私">
            <Icon name="password" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="界面">
            <Icon name="chat" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="分组">
            <Icon name="folder" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="高级">
            <Icon name="adjustment" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="外设">
            <Icon name="media" custom-class="setting_dialog_icon" />
          </OptionItem>
          <OptionItem title="省电">
            <Icon name="battery" custom-class="setting_dialog_icon" />
          </OptionItem>
        </div>
      </div>
    </template>
  </ScrollDialog>
</template>

<script setup lang="ts">
import ScrollDialog from '@renderer/components/ScrollDialog.vue'
import OptionItem from '../OptionItem.vue'
import Icon from '@renderer/components/Icon.vue'
import Avatar from '@renderer/components/Avatar.vue'

import { onMounted, onUnmounted, ref } from 'vue'

import mitt from '@renderer/utils/mitt'
import { useAppStore } from '@renderer/stores/appStore'
import { http } from '@renderer/utils/http'

const appStore = useAppStore()

const visible = ref(false)

onMounted(() => {
  mitt.on('SettingDialog:open', open)
  mitt.on('SettingDialog:close', close)
})

onUnmounted(() => {
  mitt.off('SettingDialog:open')
  mitt.off('SettingDialog:close')
})

function open() {
  visible.value = true
}

function close() {
  visible.value = false
}

function handleFile(file: File | null) {
  if (!file) return
  const data = new FormData()
  data.append('file', file)
  http({
    method: 'POST',
    url: '/profile/upload/avatar',
    data: data
  }).then((res) => {
    console.dir(res)
    if (!res.status) return
    console.log(res.data)
    appStore.setAvatar(res.data)
  })
}
</script>

<style scoped>
.setting_dialog {
  z-index: 2;
}

.content {
  background-color: var(--color-background-soft);
}

.top_bar {
  display: flex;
  align-items: center;
  background-color: var(--color-background);
}

.user_text {
  font-size: 2rem;
  margin-left: 0.5rem;
}

.avatar {
  width: 5rem;
  height: 5rem;
  margin: 0.5rem 1rem;
}

.option_box {
  background-color: var(--color-background);
  box-shadow: 0 0 0.1rem 0 var(--color-background-shadow);
}

.space {
  height: 0.5rem;
}
</style>

<style>
.setting_dialog_icon {
  width: 1.5rem;
  height: 1.5rem;
}
</style>
