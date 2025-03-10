<template></template>

<script setup lang="ts" >

import { UserManager } from 'oidc-client-ts';
import { onMounted, getCurrentInstance, inject} from 'vue';

onMounted(() =>{
  //const auth = getCurrentInstance()?.appContext.config.globalProperties.$auth
  const auth:UserManager = inject('$auth')
  auth.renewToken()
})


</script>