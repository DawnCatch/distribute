<template>
    <div class="dialog">
        <div class="masking" v-if="mask" @click="close"></div>
        <Transition :name="transition">
            <div :class="[customClass]" v-show="visible">
                <slot></slot>
            </div>
        </Transition>
    </div>
</template>

<script setup lang="ts">
defineProps({
    visible: {
        type: Boolean,
        default: false,
        required: true
    },
    mask: {
        type: Boolean,
        default: false
    },
    customClass: {
        type: String,
        default: ""
    },
    transition: {
        type: String,
        default: "dialog"
    }
})

const emit = defineEmits(["clickMaskListen"])

function close() {
    emit("clickMaskListen")
}
</script>

<style scoped>
.dialog {
    position: absolute;
    display: flex;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background-color: blur(black, .5);
    overflow: hidden;
    pointer-events: v-bind("visible ? '' : 'none'");
    z-index: 2;
}

.masking {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: -1;
    overflow: auto;
    opacity: v-bind("visible ? '1' : '0'");
    background-color: rgba(0, 0, 0, .7);
    backdrop-filter: blur(.3rem);
    pointer-events: v-bind("visible ? '' : 'none'");
    transition: all .5s;
}
</style>