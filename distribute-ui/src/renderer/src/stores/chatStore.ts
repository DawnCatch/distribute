import { defineStore } from 'pinia'

export const useChatStore = defineStore('chat', {
  state: () => ({
    peer: null as RTCPeerConnection | null,
    mediaStream: null as MediaStream | null,
    ice: null as RTCIceCandidate | null
  }),
  actions: {
    setPeer(peer: RTCPeerConnection) {
      this.peer = peer
    },
    setMediaStream(mediaStream: MediaStream) {
      this.mediaStream = mediaStream
    }
  },
  getters: {}
})
