
<script setup lang="ts">
import Auth from "src/services/oidc.service"
import { onMounted } from "vue"
import { useRouter } from "vue-router"

onMounted( () =>{
  try {
    const router = useRouter()

    if (Auth) {
      Auth.signinCallback().catch( (err) => {
        console.error("Error during signin callback: " + JSON.stringify(err))
      })
      router?.push('/')
        .catch( (err) => {
          console.error("Navigation error after signin callback: " + JSON.stringify(err))
        })
    } else {
      console.error("Auth injection failed")
    }
  } catch (e) {
    console.error(e)
  }
})

</script>


<template>
  <div class="fullscreen bg-blue text-white text-center q-pa-md flex flex-center">
    <div>
      <div style="font-size: 10vh">Authenticating ...</div>
    </div>
  </div>
</template>



