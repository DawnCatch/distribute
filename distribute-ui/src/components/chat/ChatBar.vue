<template>
    <div class="chat_bar">
        <div @click="send">
            start
        </div>
        <div v-for="(item, index) in group">
            <div class="time_stamp">
                {{ new Date(index * (5 * 60 * 1000)).toString().replace(" GMT+0800 (中国标准时间)", "") }}
            </div>
            <Message :messages="item" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { useAppStore } from '../../stores/appStore';
import { computed } from 'vue';
import mitt from '../../utils/mitt';

import Message from './Message.vue';

const appStore = useAppStore()

const group = computed(() => {
    return appStore.index !== -1 && appStore.messageGroup[appStore.relations[appStore.index].userId]
})


function send() {
    mitt.emit("rtc:request", appStore.relations[appStore.index].userId)
}

</script>

<style scoped>
.chat_bar {
    position: relative;
    background-color: var(--color-background-pro);
    height: 100%;
    width: 80%;
    overflow-y: auto;
}

.chat_bar::-webkit-scrollbar {
    width: .5rem;
    height: .5rem;
}

.chat_bar::-webkit-scrollbar-thumb {
    border-radius: 1rem;
}

.chat_bar:hover::-webkit-scrollbar-thumb {
    background: #ccc;
}

.time_stamp {
    width: 100%;
    text-align: center;
}
</style>