<template>
  <div class="dialog">
    <div v-if="mask" class="masking" @click="close"></div>
    <Transition :name="transition">
      <div v-show="visible" :class="[customClass]">
        <slot></slot>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
defineProps({
  visible: {
    type: Boolean,
    required: true
  },
  mask: {
    type: Boolean,
    default: false
  },
  customClass: {
    type: String,
    default: ''
  },
  transition: {
    type: String,
    default: 'dialog'
  }
})

const emit = defineEmits(['clickMaskListen'])

function close() {
  emit('clickMaskListen')
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
  background-color: blur(black, 0.5);
  overflow: hidden;
  pointer-events: v-bind("visible ? '' : 'none'");
  z-index: 1;
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
  background-color: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(0.3rem);
  pointer-events: v-bind("visible ? '' : 'none'");
  transition: all 0.5s;
}
</style>
