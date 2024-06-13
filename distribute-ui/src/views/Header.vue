<template>
    <div data-tauri-drag-region class="title_bar">
        <div class="chat_title" v-if="appStore.index !== -1">
            {{ appStore.relations[appStore.index].title }}
        </div>
        <div class="window_tool">
            <div class="option" @click.left="windowMinimize">
                <Icon name="minimize" custom-class="option_icon" />
            </div>
            <div class="option" @click.left="windowMaximize">
                <Icon :name="maxState ? 'enlarge' : 'narrow'" custom-class="option_icon" />
            </div>
            <div class="option highlight" @click.left="windowClose">
                <Icon name="close" custom-class="option_icon" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { appWindow } from "@tauri-apps/api/window";
import { useAppStore } from "../stores/appStore";

import Icon from "../components/Icon.vue";

const appStore = useAppStore()

const maxStateName = ref('window-maximize')
const maxState = ref(false)

watch(maxState, async (newValue) => {
    if (newValue) {
        maxStateName.value = 'window-restore'
        await appWindow.maximize()
    } else {
        maxStateName.value = 'window-maximize'
        await appWindow.unmaximize()
    }
})

async function windowMinimize() {
    await appWindow.minimize()
}

function windowMaximize() {
    maxState.value = !maxState.value
}

async function windowClose() {
    await appWindow.hide()
}
</script>

<style scoped>
.title_bar {
    display: flex;
    background-color: var(--color-background-mute);
}

.chat_title {
    line-height: 1.5rem;
    margin-left: .5rem;
}

.window_tool {
    display: flex;
    margin-left: auto;
}

.option {
    display: flex;
    color: var(--color-edit);
    padding: .25rem 1rem;
    justify-content: center;

}

.option:hover {
    color: var(--color-text);
    background-color: var(--color-background-soft);
}

.highlight:hover {
    background-color: var(--color-warning)
}
</style>

<style>
.option_icon {
    width: 1rem;
    height: 1rem;
}
</style>