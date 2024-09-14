<template>
  <div class="edit_content">
    <label :class="placeholderTextClass">
      <span>{{ placeholder }}</span>
    </label>
    <div>
      <form onsubmit="return false">
        <input :type="type" :value="modelValue" autocomplete="“off”" @input="handleInput" />
      </form>
    </div>
    <div class="line_box">
      <div class="line line-focus"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
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

const placeholderTextClass = computed(() => {
  return {
    placeholder_text: !props.modelValue || props.modelValue === '',
    placeholder_text_focus: props.modelValue && props.modelValue !== ''
  }
})

function handleInput(e: Event) {
  emit('update:modelValue', e.target.value as string)
}
</script>

<style scoped>
.edit_content {
  height: 3rem;
  width: 100%;
}

.placeholder_text,
.placeholder_text_focus {
  display: block;
  pointer-events: none;
  position: relative;
  line-height: 1rem;
  text-align: left;
  color: var(--color-edit);
  z-index: 0;
}

.placeholder_text {
  display: block;
  top: 1rem;
  font-size: 1rem;
  transition: all 0.3s;
}

.placeholder_text_focus {
  font-size: 0.75rem;
  top: 0;
}

.edit_content:focus-within > .placeholder_text {
  font-size: 0.75rem;
  top: 0;
  color: var(--color-edit-focus);
}

.edit_content:focus-within > .placeholder_text_focus {
  color: var(--color-edit-focus);
}

input {
  padding: 0;
  outline: none;
  background: transparent;
  border: none;
  outline: medium;
  width: 100%;
  font-size: 1rem;
  color: var(--color-edit);
  caret-color: var(--color-edit-focus);
}

input:focus {
  outline: none;
  background-color: transparent;
}

.line_box,
.line {
  height: 2px;
}

.line_box {
  width: 100%;
  background-color: var(--color-edit);
}

.line {
  background-color: var(--color-edit-focus);
  margin: auto;
  width: 0%;
  transition: all 0.3s;
}

.edit_content:focus-within > .line_box > .line {
  width: 100%;
}

.line_focus {
  width: 100%;
}

.placeholder {
  position: absolute;
  top: 0;
  left: 0;
  font-size: var(--field-decorator-placeholder-size);
  line-height: 1.5em;
  color: var(--field-decorator-blur-color);
  pointer-events: none;
  cursor: inherit;
  transform-origin: left;
  transition:
    transform 0.28s var(--cubic-bezier),
    color 0.25s,
    max-width 0.2s;
}
</style>
