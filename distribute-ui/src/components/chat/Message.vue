<template>
    <div class="message_bar" :class="{ reverse: reverse }">
        <div class="avatar_box" v-if="!reverse">
            <img src="../../assets/avatar.jpg" alt="avatar" v-if="appStore.profile.nickname">
            <Icon name="avatar" customClass="avatar_default" v-else />
        </div>
        <div class="message_box">
            <div class="content_bar" v-for="(message) in (messages as Message[])">
                <div v-html="md2Ele(message.content)"></div>
                <span class="space"></span>
                <span class="time">{{ getTime(message.date) }}</span>
            </div>
        </div>
        <div class="avatar_box" v-if="reverse">
            <img src="../../assets/avatar.jpg" alt="avatar" v-if="appStore.profile.nickname">
            <Icon name="avatar" customClass="avatar_default" v-else />
        </div>
    </div>
</template>

<script setup lang="ts">
import { useAppStore, Content, Message } from "../../stores/appStore";
import markdown from "../../utils/markdown";

import Icon from "../Icon.vue";
import { ref } from "vue";

const appStore = useAppStore()

defineProps({
    messages: {
        type: Array,
        required: true
    }
})

const reverse = ref(false)

function getTime(time: number) {
    const date = new Date(time)
    var result = ""
    var hours = (date.getHours() + 12) % 24
    var minutes = date.getMinutes()
    if (hours < 10) result = `0${hours}:`
    else result = `${hours}:`
    if (minutes < 10) result += `0${minutes}`
    else result += `${minutes}`
    return result
}

function md2Ele(item: Content) {
    return markdown.render(item.value)
}
</script>

<style scoped>
.message_bar {
    display: flex;
    padding: .25rem 1rem 1rem 1rem;
    align-items: flex-end;
}

.reverse {
    justify-content: flex-end;
}

.avatar_box {
    display: flex;
    position: sticky;
    bottom: 0;
    height: 3rem;
    width: 3rem;
    margin-bottom: 0.3rem;
    background-color: aquamarine;
    border-radius: 50%;
    overflow: hidden;
}

.message_box {
    display: flex;
    flex-direction: column;
    max-width: calc(100% - 8rem);
    padding: 0 1rem;
}

.content_bar {
    display: flex;
    position: relative;
    padding: 1rem 1rem;
    margin: .1rem 0;
    word-wrap: break-word;
    border-radius: 1rem;
    background-color: var(--color-background-mute);
}

.space {
    display: inherit;
    width: 1.5rem;
}

.time {
    position: absolute;
    right: .5rem;
    bottom: .5rem;
    font-size: .5rem;
    color: var(--color-edit);
}
</style>