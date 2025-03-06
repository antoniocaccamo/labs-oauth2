<template></template>

<script setup lang="ts" >

import { onMounted, getCurrentInstance } from 'vue';

onMounted(() =>{
  const auth = getCurrentInstance()?.appContext.config.globalProperties.$auth
  auth.renewToken()
})


</script>