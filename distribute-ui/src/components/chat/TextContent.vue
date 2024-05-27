<template>
    <span class="text_content">
        {{ content.value }}
    </span>
</template>

<script setup lang="ts">
defineProps({
    content: {
        type: Object,
        required: true
    },
    from: {
        type: Number,
        required: true
    }
})
</script>

<style scoped></style>