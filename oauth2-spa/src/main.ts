import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Auth from '@/auth'

import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
app.config.globalProperties.$auth=Auth