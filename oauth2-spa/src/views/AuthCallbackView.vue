
<script setup lang="ts">
import {getCurrentInstance, onMounted } from "vue"
 

onMounted( () =>{
  try {
    const auth = getCurrentInstance()?.appContext.config.globalProperties.$auth
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