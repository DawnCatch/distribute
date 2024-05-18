<template>
    <div data-tauri-drag-region class="title_bar">
        <div class="window_tool">
            <div class="option" @click.left="windowMinimize">
                <Icon name="minimize" />
            </div>
            <div class="option" @click.left="windowMaximize">
                <Icon :name="maxState ? 'enlarge' : 'narrow'" />
            </div>
            <div class="option highlight" @click.left="windowClose">
                <Icon name="close" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { appWindow } from "@tauri-apps/api/window";

import Icon from "../components/Icon.vue";

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
    await appWindow.close()
}
</script>

<style scoped>
.title_bar {
    background-color: var(--color-background-mute);
}

.window_tool {
    float: right;
    display: flex;
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