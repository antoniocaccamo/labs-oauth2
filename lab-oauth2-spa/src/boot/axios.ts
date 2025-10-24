import { defineBoot } from '#q-app/wrappers';
import axios, { type AxiosInstance } from 'axios';
import { v4 as uuid4 } from 'uuid';

import { useOidcStore } from 'src/stores/oidc-store';
import { watch } from 'vue';

declare module 'vue' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
    $api: AxiosInstance;
  }
}

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)
const api = axios.create({
    baseURL: process.env.LAB_API_BASE_URL,
});


function getOrCreateTraceId() {
  let traceId : string | null = localStorage.getItem('traceId');
  if (!traceId) {
      traceId = uuid4();  // Generate a new UUID for the traceId
      localStorage.setItem('traceId', traceId?? '');  // Store it for future requests
  }
  return traceId;
}

function addBearer(token: string) {
  console.info('::api:: adding bearer token..');

  api.defaults.headers.common['Authorization'] = 'Bearer ' + token;
  api.defaults.headers.common['X-Trace-Id'] = getOrCreateTraceId();
}

function removeBearer() {
  api.defaults.headers.common['Authorization'] = '';
  console.info('::api:: bearer token removed');
}

export default defineBoot(({ app }) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api

  app.config.globalProperties.$axios = axios;
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  api.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
  app.config.globalProperties.$api = api;
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
});

const oidcUser = useOidcStore();

watch(
  () => oidcUser?.isAuthenticated,
  () => {
    if (oidcUser?.user?.access_token) {
      addBearer(oidcUser?.user?.access_token);
    } else {
    removeBearer();
    }
});

export { api };

