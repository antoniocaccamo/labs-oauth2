import { defineBoot } from '#q-app/wrappers';
import Auth  from 'src/services/oidc.service';

export default defineBoot(({ app }) => {
  app.provide('oidcService',  Auth );
});
