import { defineBoot } from '#q-app/wrappers';
import { useOidcStore } from 'src/stores/oidc-store';

export default defineBoot(({ router }) => {
  const oidcStore = useOidcStore();

  router.beforeResolve((to, from, next) => {
    if (to.meta?.authenticated && !oidcStore.isAuthenticated) {
      next({ path: '/forbidden' });
      return;
    }
    // const authorities: string | string[] = <string | string[]>to.meta?.authorities;

    // if (authorities && authorities.length > 0) {
    //   const value = oidcStore.hasAnyAuthorityAndCheckAuth(authorities);
    //   if (!value) {
    //     if (from.path !== '/forbidden') {
    //       next({ path: '/forbidden' });
    //       return;
    //     }
    //   }
    // }
    next();
  });
});
