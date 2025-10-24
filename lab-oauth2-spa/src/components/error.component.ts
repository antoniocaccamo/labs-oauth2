
import { defineComponent, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute } from 'vue-router';


export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ErrorComponent',
  setup() {
    // const oidcService = inject<OidcService>('oidcService');
    // const authenticated = computed( () => store.isAuthenticated );
    const errorMessage = ref();
    const error403 = ref(false);
    const error404 = ref(false);
    const route = useRoute();

    if (route.meta) {
      errorMessage.value = <string>route.meta.errorMessage ?? undefined;
      error403.value = <boolean>route.meta.error403 ?? false;
      error404.value = <boolean>route.meta.error404 ?? false;
      console.info('ErrorComponent: ' + JSON.stringify(route));
    }

    return {
      errorMessage,
      error403,
      error404,
      t$: useI18n().t,
    };
  },
});
