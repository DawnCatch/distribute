<template>
  <div class="search_item">
    <Avatar class="avatar" :type="item.type" />
    <div class="title">{{ profile?.title ?? 'None' }}</div>
    <div class="option_box" @click="option.work(item.id)">
      <div>{{ option.title }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'

import Avatar from '@renderer/components/Avatar.vue'

import { GroupProfile, Profile, useAppStore, UserProfile } from '@renderer/stores/appStore'
import { http } from '@renderer/utils/http'

const appStore = useAppStore()

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const profile = ref<Profile | null>()

const option = computed(() => {
  const relation = profile.value?.relation
  const type = props.item.type
  if (relation === undefined) return { title: '...', work: () => true }
  return options[relation][type ? 1 : 0]
})

function follow(id: number) {
  http({
    method: 'POST',
    url: '/relation/user/follow',
    data: {
      targetId: id
    }
  }).then((res) => {
    if (!res.status) return
    const message = res.message
    if (message === '关注成功') {
      appStore.addFollows(id)
    } else if (message === '取消关注') {
      appStore.removeFollows(id)
    }
  })
}

function application(id: number) {
  http({
    method: 'POST',
    url: '/relation/group/application',
    data: {
      targetId: id
    }
  }).then((res) => {
    if (!res.status) return
    const message = res.message
    if (message === '申请成功') {
      appStore.addFollows(id)
    } else if (message === '取消成功') {
      appStore.removeFollows(id)
    }
  })
}

const options = {
  '=': [
    {
      title: '取消关注',
      work: follow
    },
    {
      title: '退出群组',
      work: (id: number) => { }
    }
  ],
  '>': [
    {
      title: '取消关注',
      work: follow
    },
    {
      title: '取消申请',
      work: application
    }
  ],
  '<': [
    {
      title: '回关',
      work: follow
    },
    {
      title: '同意邀请',
      work: (id: number) => { }
    }
  ],
  '!': [
    {
      title: '关注',
      work: follow
    },
    {
      title: '发送申请',
      work: application
    }
  ],
} as Record<string, Option[]>

watch(
  () => appStore.profiles,
  (newVal) => {
    const { type, id } = props.item as SearchResultItem
    profile.value = newVal.find((it) => it.type === type && it.id === id)
  },
  { deep: true }
)

onMounted(() => {
  const { type, id } = props.item as SearchResultItem
  if (profile.value && profile.value.title !== 'None') return
  if (type) {
    http({
      url: `/group/get/${id}`
    }).then((res) => {
      if (!res.status) return
      appStore.addGroupProfile(res.data as GroupProfile)
    })
  } else {
    http({
      url: `/profile/get/${id}`
    }).then((res) => {
      if (!res.status) return
      appStore.addUserProfile(res.data as UserProfile)
    })
  }
})
</script>

<style scoped>
.search_item {
  display: flex;
  align-items: center;
  height: 4rem;
}

.avatar {
  height: 3rem;
  width: 3rem;
}

.title {
  margin-left: 1rem;
}

.option_box {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: auto;
  margin-right: 1rem;
  width: 5rem;
  height: 2rem;
  border-radius: 0.5rem;
  background-color: var(--color-background-soft);
  cursor: pointer;
}

.option_box:hover {
  background-color: var(--color-background-mute);
}
</style>
