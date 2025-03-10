import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
import VueAxios from 'vue-axios'

import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(VueAxios, axios)



import Auth from '@/auth'
// declare module 'vue' {
//     interface ComponentCustomProperties {
//       $$auth: typeof Auth
//       $translate: (key: string) => string
//     }
//   }


app.config.globalProperties.$auth=Auth

app.provide('$auth', app.config.globalProperties.$auth)
app.provide('axios', app.config.globalProperties.axios) 

app.mount('#app')