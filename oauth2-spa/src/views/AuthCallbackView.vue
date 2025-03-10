
<script setup lang="ts">
import { UserManager } from "oidc-client-ts"
import {getCurrentInstance, onMounted, inject } from "vue"
 

onMounted( () =>{
  try {
    // const auth = getCurrentInstance()?.appContext.config.globalProperties.$auth
    const auth:UserManager = inject('$auth')
    const router = getCurrentInstance()?.appContext.config.globalProperties.$router
    auth.signinCallback()
    router?.push('/')
  } catch (e) {
    console.error(e)
  }
})

</script>


<template>
  <div>Authenticating...</div>
</template>