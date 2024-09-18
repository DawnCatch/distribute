<template>
  <ScrollBox class="chat_detail_box">
    <div class="avatar_box">
      <Avatar />
    </div>
    <div class="space"></div>
    <div class="option_box">
      <OptionItem title="通知" type="switch">
        <Icon name="toast-2" custom-class="chat_detail_icon" />
      </OptionItem>
    </div>
    <div class="space"></div>
    <div class="option_box">
      <OptionItem v-if="appStore.current.type" class="important" title="退出">
        <Icon name="exit" custom-class="chat_detail_icon" />
      </OptionItem>
      <OptionItem v-else class="important" title="取消关注" :use="exit">
        <Icon name="delete" custom-class="chat_detail_icon" />
      </OptionItem>
    </div>
  </ScrollBox>
</template>

<script setup lang="ts">
import { useAppStore } from '@renderer/stores/appStore'
import { useRelationStore } from '@renderer/stores/relationStore'

import { computed } from 'vue'

import ScrollBox from '../ScrollBox.vue'
import Avatar from '../Avatar.vue'
import OptionItem from '../side/OptionItem.vue'
import Icon from '../Icon.vue'
import { follow } from '@renderer/utils/http'

const appStore = useAppStore()
const relationStore = useRelationStore()

computed(() => {
  const { type, id } = appStore.current
  if (!type) return 0
  const members = relationStore.members(id)
  return members
})

function exit() {
  follow(appStore.current.id)
}
</script>

<style scoped>
.chat_detail_box {
  display: flex;
  flex-direction: column;
  width: 14rem;
  border-left: 0.125rem solid var(--color-background-mute);
  background-color: var(--color-background-soft);
}

.avatar_box {
  padding: 1rem 4rem;
  box-shadow: 0 0 0.1rem 0 var(--color-background-shadow);
  background-color: var(--color-background);
}

.option_box {
  background-color: var(--color-background);
  box-shadow: 0 0 0.1rem 0 var(--color-background-shadow);
}

.space {
  height: 0.5rem;
}

.important {
  color: red;
}
</style>

<style>
.chat_detail_icon {
  width: 1.5rem;
  height: 1.5rem;
}
</style>
