import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { User, UserManager } from 'oidc-client-ts'

export const useOidcUser = defineStore('oidc-user', () => {

  const user = ref<User>()
  const UserManager = ref<UserManager>()


  const isAuthenticated = computed( () => user.value !== undefined  )

  function setUser(aUser : User) {
    user.value = aUser
  }
  
  function reset() {
    user.value = undefined
  }

  return { user, isAuthenticated , setUser, reset}
})
