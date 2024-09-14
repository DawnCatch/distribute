import './styles.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import VueDOMPurifyHTML from 'vue-dompurify-html'
import piniaPersist from 'pinia-plugin-persist'

import App from './App.vue'

import router from './router/index'

const app = createApp(App)

const pinia = createPinia()
pinia.use(piniaPersist)

app.use(pinia)
app.use(router)
app.use(VueDOMPurifyHTML)

app.mount('#app')
