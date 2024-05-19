import "./styles.css";

import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";

import router from "./router";
import mitt from "./utils/mitt";

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount("#app");

// declare module "@vue/runtime-core" {
//     interface ComponentCustomProperties {
//         $mitt: Emitter<Record<EventType, unknown>>;
//     }
// }
