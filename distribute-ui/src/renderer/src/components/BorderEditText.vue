<template>
  <div class="edit_content" :class="{ focus: focused }">
    <form @onsubmit.prevent>
      <input ref="inputRef" :type="type" :value="modelValue" autocomplete="off" :placeholder @input="handleInput" />
    </form>
    <div class="option_button" @click="clear">
      <Icon name="close" custom-class="clear" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Icon from './Icon.vue'
import { useFocus } from '@vueuse/core'

defineProps({
  modelValue: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    default: 'placeholder'
  },
  type: {
    type: String,
    default: 'text'
  }
})

const emit = defineEmits(['update:modelValue'])

function handleInput(e: Event) {
  emit('update:modelValue', (e.target as HTMLInputElement).value)
}

const inputRef = ref<HTMLInputElement | null>(null)

const { focused } = useFocus(inputRef, { initialValue: false })

function clear() {
  focused.value = false
  emit('update:modelValue', '')
}

function focus(is: boolean) {
  focused.value = is
}

defineExpose({
  focus,
  focused
})
</script>

<style scoped>
.edit_content {
  background-color: var(--color-border-edit-text-background);
  border: 2px var(--color-border-edit-text-border) solid;
  padding: 0.25rem 0.5rem;
  border-radius: 20px;
  width: 100%;
  position: relative;
  display: flex;
  align-items: center;
}

.focus {
  background-color: var(--color-border-edit-text-background-focus);
}

form {
  flex: 1;
}

input {
  font-size: medium;
  appearance: none;
  border: none;
  background-color: transparent;
  padding: 0;
  outline: none;
  width: 100%;
}

.option_button {
  flex-shrink: 0;
  width: 1rem;
  height: 1rem;
  overflow: hidden;
  cursor: pointer;
}
</style>

<style>
.clear {
  display: v-bind("focused ? 'block' : 'none'");
  width: 1rem;
  height: 1rem;
  color: var(--color-border-edit-text-icon);
}
</style>
