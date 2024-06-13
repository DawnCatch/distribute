<template>
    <div class="profile_box" @click="select">
        {{ relation.title }}
    </div>
</template>

<script setup lang="ts">
import { useAppStore } from '../../stores/appStore';

const appStore = useAppStore()

const props = defineProps({
    relation: {
        type: Object,
        required: true
    },
    index: {
        type: Number,
        required: true
    }
})

function select() {
    appStore.setIndex(props.index)
}
</script>
<style scoped>
.profile_box {

}
</style>