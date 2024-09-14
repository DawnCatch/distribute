<template>
  <div ref="chatOptionBar" class="chat_option_bar" tabindex="0">
    <div class="file_box">文件</div>
    <div class="input_box">
      <textarea
        ref="textarea"
        v-model="input"
        class="input"
        placeholder="输入消息..."
        @keydown="handleKeyCode($event)"
        @keydown.tab="handleKeyCode($event)"
      />
      <div
        v-show="mode === Mode.DOCK && preview"
        ref="dock"
        v-dompurify-html="preview"
        class="preview_box dock"
      ></div>
    </div>
    <div
      v-show="focused && mode === Mode.FLOAT && preview"
      ref="perviewBox"
      v-dompurify-html="preview"
      class="preview_box float"
      :style="style"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, ref } from 'vue'
import {
  useElementSize,
  useTextareaAutosize,
  useFocusWithin,
  watchDebounced,
  useDraggable
} from '@vueuse/core'
import { http } from '../../utils/http'
import markdown from '../../utils/markdown'
import { useAppStore } from '../../stores/appStore'

const appStore = useAppStore()

const { textarea, input } = useTextareaAutosize()
const { height } = useElementSize(textarea)

const mode = ref(Mode.FLOAT)

const chatOptionBar = ref()
const { focused } = useFocusWithin(chatOptionBar)

function handleKeyCode(event: KeyboardEvent) {
  if (event.key === 'Enter' && event.ctrlKey && input.value !== '') {
    http({
      url: `/message/${appStore.checkItem.type ? 'group' : 'user'}/send`,
      data: {
        to: appStore.checkItem.id,
        content: {
          type: 'TEXT',
          value: input.value
        }
      }
    })
    input.value = ''
  } else if (event.key === 'Tab') {
    if (!input.value) input.value = '    '
    const selectionStart = textarea.value.selectionStart
    input.value =
      input.value.substring(0, selectionStart) + '    ' + input.value.substring(selectionStart)
    nextTick(() => {
      textarea.value.selectionStart = selectionStart + 4
      textarea.value.selectionEnd = selectionStart + 4
    })
    event.preventDefault()
  }
}

const preview = ref('')
const perviewBox = ref<HTMLElement | null>(null)

const { style } = useDraggable(perviewBox, {
  initialValue: { x: 160, y: 32 }
})

watchDebounced(
  input,
  () => {
    preview.value = markdown.render(input.value)
  },
  { debounce: 500, maxWait: 1000 }
)
</script>

<script lang="ts">
enum Mode {
  NONE = 0,
  DOCK = 1,
  FLOAT = 2,
  WINDOW = 3
}
</script>

<style scoped>
.chat_option_bar {
  display: flex;
  position: fixed;
  width: 80%;
  bottom: 0;
  font-size: 1.25rem;
  line-height: 2rem;
  background-color: var(--color-background-soft);
  z-index: 1;
}

.file_box {
  height: 3rem;
  width: 3rem;
  padding: 0.5rem;
}

.input_box {
  display: flex;
  margin: 0.5rem;
  width: calc(100% - 4rem);
  height: 100%;
}

.input {
  width: v-bind("mode === Mode.DOCK ? '50%' : '100%'");
  max-height: 10rem;
  resize: none;
  font-size: 1rem;
  outline: none;
  background-color: var(--color-background-pro);
  border-radius: 0.75rem;
  padding-left: 0.5rem;
  padding-right: 0.5rem;
  color: var(--color-text);
  border: 0;
  line-height: 1.75rem;
}

.input::-webkit-scrollbar {
  width: 0.5rem;
  height: 0.5rem;
}

.input::-webkit-scrollbar-thumb {
  border-radius: 1rem;
}

.input:hover::-webkit-scrollbar-thumb {
  background: #ccc;
}

.preview_box {
  padding-left: 0.5rem;
  min-width: 20%;
  max-width: 50%;
  max-height: 50%;
  overflow: auto;
}

.preview_box::-webkit-scrollbar {
  width: 0.5rem;
  height: 0.5rem;
}

.preview_box::-webkit-scrollbar-thumb {
  border-radius: 1rem;
}

.preview_box:hover::-webkit-scrollbar-thumb {
  background: #ccc;
}

.dock {
  width: 50%;
  height: v-bind('`${height}px`');
}

.float {
  background-color: var(--color-background-mute);
  border: 1px solid var(--color-background);
  position: fixed;
}
</style>
