import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'


import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')


import Auth from '@/auth'
declare module 'vue' {
    interface ComponentCustomProperties {
      $$auth: typeof Auth
      $translate: (key: string) => string
    }
  }


app.config.globalProperties.$auth=Auth