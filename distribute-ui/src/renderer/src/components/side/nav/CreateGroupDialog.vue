<template>
  <ScrollDialog class="create_group_dialog" :visible="visible" @click-mask-listen="close">
    <template #content>
      <div class="content">
        <div class="form">
          <Avatar class="avatar" :src="avatarUrl" edit @check-file="handleFile" />
          <EditText v-model="groupName" class="edit" placeholder="用户名" />
        </div>
        <div class="button_box">
          <div class="space"></div>
          <div @click="close">取消</div>
          <div v-if="!isWait" @click="commit">下一步</div>
          <div v-else>处理中...</div>
        </div>
      </div>
    </template>
  </ScrollDialog>
</template>

<script setup lang="ts">
import mitt from '@renderer/utils/mitt'

import { computed, onMounted, onUnmounted, ref, watch } from 'vue'

import ScrollDialog from '@renderer/components/ScrollDialog.vue'
import Avatar from '@renderer/components/Avatar.vue'
import EditText from '@renderer/components/EditText.vue'
import { useRelationStore } from '../../../stores/relationStore'
import { http } from '@renderer/utils/http'

const visible = ref(false)

onMounted(() => {
  mitt.on('CreateGroupDialog:open', open)
  mitt.on('CreateGroupDialog:close', close)
})

onUnmounted(() => {
  mitt.off('CreateGroupDialog:open')
  mitt.off('CreateGroupDialog:close')
})

function open() {
  visible.value = true
}

function close() {
  visible.value = false
}

const relationStore = useRelationStore()

const groupName = ref('')

const groupAvatar = ref<File | null>(null)
const avatarUrl = computed(() => {
  if (!groupAvatar.value) return ''
  return URL.createObjectURL(groupAvatar.value)
})

function handleFile(file: File | null) {
  if (!file) return
  groupAvatar.value = file
}

const isWait = ref(false)

const groupId = ref(-1)

function commit() {
  if (groupName.value === '' && isWait) return
  isWait.value = true
  http({
    method: 'POST',
    url: '/group/create',
    data: {
      title: groupName.value,
      visible: true
    }
  }).then((res) => {
    if (!res.status) return
    const { groupId: id } = res.data
    groupId.value = id
    relationStore.getGroups()
  })
}

watch(groupId, (newVal) => {
  if (!groupAvatar.value || newVal === -1) return
  const data = new FormData()
  data.append('id', `${newVal}`)
  data.append('file', groupAvatar.value)
  http({
    method: 'POST',
    url: '/group/upload/avatar',
    data: data
  }).then((res) => {
    if (!res.status) return
    relationStore.setGroupAvatar(groupId.value, res.data)
  })
})
</script>

<style scoped>
.create_group_dialog {
  z-index: 2;
}

.content {
  padding: 1rem;
}

.form {
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 4rem;
  height: 4rem;
  margin-right: 1rem;
}

.edit {
  flex: 1;
}

.button_box {
  display: flex;
}

.space {
  flex: 1;
}
</style>
