<template>
  <div class="btn">
    <div v-if="loading" class="loading"></div>
    <div v-else class="text">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const loading = ref(false)

function wait() {
  loading.value = true
}

function release() {
  loading.value = false
}

defineExpose({
  wait,
  release
})
</script>

<style scoped>
.btn {
  display: flex;
  position: relative;
  text-align: center;
  height: 2.5rem;
  border-radius: 2rem;
  background-color: var(--color-button);
  line-height: 2.5rem;
  cursor: pointer;
  overflow: hidden;
  transition: background-color 0.25s;
}

.btn::after {
  content: '';
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  position: absolute;
  background-image: radial-gradient(circle, #ccc 10%, transparent 10.1%);
  transform: scale(10);
  opacity: 0;
  transition: all 0.6s;
}

.btn:active::after {
  transform: scale(0);
  opacity: 0.5;
  transition: 0s;
}

.btn:hover {
  background-color: var(--color-button-hover);
}

.loading {
  margin: auto;
  width: 1.5rem;
  height: 1.5rem;
  border: 3px solid #000;
  border-top-color: transparent;
  border-radius: 100%;
  animation: circle infinite 0.75s linear;
}

@keyframes circle {
  0% {
    transform: rotate(0);
  }

  100% {
    transform: rotate(360deg);
  }
}

.text {
  margin: auto;
}
</style>
