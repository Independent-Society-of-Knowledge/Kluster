import { createApp } from 'vue'
import './style/index.scss'
import App from "./App.vue";
import naive from "naive-ui"

createApp(App)
    .use(naive)
    .mount('#app')
