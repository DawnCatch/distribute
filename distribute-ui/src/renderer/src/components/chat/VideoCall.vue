<template>
  <div v-show="false" ref="videoCallBox" class="video_call_box" :style="style">
    <div>
      <video ref="localVideo" autoplay></video>
      <video ref="remoteVideo" autoplay></video>
    </div>
    <div @click="hangUp">挂断</div>
  </div>
</template>

<script setup lang="ts">
import { useDisplayMedia, useDraggable } from '@vueuse/core'
import { onMounted, ref } from 'vue'
import mitt from '../../utils/mitt'
import { createAnswer, createPeer } from '../../utils/rtc'
import { http } from '../../utils/http'
import { Message, useAppStore } from '../../stores/appStore'

const appStore = useAppStore()

const videoCallBox = ref<HTMLElement | null>()
const localVideo = ref<HTMLVideoElement | null>()
const remoteVideo = ref<HTMLVideoElement | null>()

const { start, stop } = useDisplayMedia()

const { style } = useDraggable(videoCallBox, {
  initialValue: { x: 40, y: 40 }
})

let peer: RTCPeerConnection | null
const ices = {} as Record<number, RTCIceCandidateInit>
let wait: boolean = false

function hangUp() {
  stop()
  console.log('挂断')
}

onMounted(() => {
  mitt.on('rtc:request', (userId) => {
    if (wait) return
    wait = true
    start().then((stream: MediaStream | undefined) => {
      localVideo.value &&
        remoteVideo.value &&
        stream &&
        createPeer(userId as number, stream, localVideo.value, remoteVideo.value).then(
          (connect) => {
            peer = connect
          }
        )
    })
  })
  mitt.on('rtc:hangUp', () => {
    hangUp()
    peer?.close()
    peer = null
    wait = false
  })
  mitt.on('rtc:callback', (e) => {
    if (wait) return
    const data = e as any
    console.log(data)
    if (data.status) {
      wait = true
      start().then((stream: MediaStream | undefined) => {
        createAnswer(
          data.userId,
          stream!,
          data.session,
          localVideo.value!,
          remoteVideo.value!
        ).then((connect) => {
          peer = connect
        })
      })
    } else {
      http({
        url: '/message/send',
        data: {
          to: data.userId,
          content: [
            {
              type: 'RTC:CB',
              value: false
            }
          ]
        }
      })
    }
  })
  mitt.on('rtc:message', (data) => {
    const message = data as Message
    const userId = message.from
    const content = message.content
    if (content.type.indexOf('RTC') === -1) return
    if (userId === appStore.own.userId) {
      if (content.type === 'RTC:REQ' && !wait) {
        console.log('wait')
      }
    } else {
      if (content.type === 'RTC:ICE') {
        ices[userId] = JSON.parse(content.value)
        peer?.addIceCandidate(JSON.parse(content.value))
      } else if (content.type === 'RTC:CB') {
        if (content.value !== 'false') {
          peer?.setRemoteDescription(JSON.parse(content.value))
          peer?.addIceCandidate(ices[userId])
        }
      }
    }
  })
})
</script>

<style scoped>
.video_call_box {
  position: fixed;
  background-color: var(--color-background);
}

video {
  height: 13rem;
  width: 20rem;
}
</style>
