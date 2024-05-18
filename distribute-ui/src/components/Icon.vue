<template>
    <svg :class="['icon', customClass]" aria-hidden="true">
        <use :xlink:href="href"></use>
    </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue';

import "../assets/icon/iconfont.js"
import "../assets/icon/iconfont.css"

const props = defineProps({
    name: {
        type: String,
        required: true
    },
    customClass: {
        type: String,
        default: ""
    }
})

const href = computed(() => `#icon-${props.name}`)
</script>

<style scoped>
.icon {
    fill: currentColor;
    overflow: hidden;
    width: 16px;
    height: 16px;
}
</style>