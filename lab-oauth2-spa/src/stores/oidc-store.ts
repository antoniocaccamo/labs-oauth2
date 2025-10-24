import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

import type { User } from 'oidc-client-ts'
import { useQuasar } from 'quasar';

export const useOidcStore = defineStore('oidcUser', () => {

  const $q = useQuasar();

  const isDark = ref(false);
  const user = ref<User>()

  const isAuthenticated = computed( () => user.value !== undefined  )

  const currentUsername = computed( () => user.value?.profile.name )
  const currentEmail = computed( () => user.value?.profile.email )

  function setUser(aUser : User) {
    user.value = aUser
  }

  function reset() {
    user.value = undefined
  }

  function toggleDark() {
    $q.dark.toggle();
    isDark.value = $q.dark.isActive;
  }

  return { user, isAuthenticated , currentUsername, currentEmail, setUser, reset, isDark, toggleDark}
})
