<template>
  <div class="navigation_option_item" @click="work">
    <slot></slot>
    <div class="option_text">{{ title }}</div>
    <div v-if="type === 'switch'" class="right_box">
      <div ref="switchBarRef" class="switch_bar" :class="is ? 'bar_true' : 'bar_false'">
        <div class="switch_ball" :class="is ? 'ball_true' : 'ball_false'"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAnimate } from '@vueuse/core'
import { ref } from 'vue'

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  type: {
    type: String,
    default: 'button'
  },
  defaultValue: {
    type: Boolean,
    default: false
  },
  use: {
    type: Function,
    default: (_: OptionItemEvent) => true
  }
})

const is = ref(props.defaultValue)

function work() {
  if (props.type === 'button') {
    props.use()
  } else {
    reverseSwitch()
  }
}

function reverseSwitch() {
  const event = {
    value: !is.value
  }
  if (props.use(event)) is.value = !is.value
  else play()
}

const switchBarRef = ref<HTMLElement | null>()
const { play } = useAnimate(
  switchBarRef,
  [
    { transform: 'translateX(0)' },
    { transform: 'translateX(0.25rem)' },
    { transform: 'translateX(0)' },
    { transform: 'translateX(0.25rem)' },
    { transform: 'translateX(0)' }
  ],
  250
)
</script>

<script lang="ts">
interface OptionItemEvent {
  value: string
}
</script>

<style scoped>
.navigation_option_item {
  display: flex;
  padding: 0.75rem 1rem;
  cursor: pointer;
  align-items: center;
}

.navigation_option_item:hover {
  background-color: var(--color-side-bar-item-background-hover);
}

.option_text {
  margin-left: 0.75rem;
  font-weight: bolder;
}

.right_box {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.switch_bar {
  display: flex;
  align-items: center;
  height: 1rem;
  width: 2rem;
  border-radius: 0.5rem;
}

.bar_true {
  background-color: var(--color-switch-background-true);
}

.bar_false {
  background-color: var(--color-switch-background-false);
}

.switch_ball {
  height: 1.25rem;
  width: 1.25rem;
  border-radius: 50%;
  background-color: var(--color-background-mute);
}

.ball_true {
  transform: translateX(0.9rem);
  border: 0.15rem solid var(--color-switch-background-true);
}

.ball_false {
  transform: translateX(-0.15rem);
  border: 0.15rem solid var(--color-switch-background-false);
}

.switch_bar,
.switch_ball {
  transition: all ease-in 150ms;
}
</style>
