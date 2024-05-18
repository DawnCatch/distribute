import "./styles.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import mitt, { Emitter, EventType } from "mitt";

import App from "./App.vue";

import router from "./router";
import {http, TauriFetch} from "./utils/http";

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.config.globalProperties.$http = http;
app.config.globalProperties.$mitt = mitt();

app.mount("#app");

declare module "@vue/runtime-core" {
    interface ComponentCustomProperties {
        $http: TauriFetch;
        $mitt: Emitter<Record<EventType, unknown>>;
    }
}
