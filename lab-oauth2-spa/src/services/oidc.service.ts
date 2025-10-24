import { UserManager, WebStorageStateStore , Log} from 'oidc-client-ts'
import { useOidcStore } from 'src/stores/oidc-store';
import { appConfig } from 'src/config';

Log.setLevel(Log.WARN)
Log.setLogger(console);


const Auth = new UserManager({
  authority: `https://login.microsoftonline.com/${appConfig.oauth2.tenantId}/v2.0`,
  // metadata :  {
  //   authorization_endpoint : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/auth`,
  //   token_endpoint         : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/token`,
  //   userinfo_endpoint      : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/userinfo`,
  //   issuer                 : `${kc.authorityUrl}/realms/${kc.realm}`,
  //   jwks_uri               : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/certs`
  // },
  client_id: `${appConfig.oauth2.clientId}`,
  redirect_uri: `${window.location.origin}/auth/callback`,
  silent_redirect_uri: `${window.location.origin}/silent-refresh`,
  post_logout_redirect_uri: `${window.location.origin}`,
  response_type: 'code',
  response_mode: 'fragment',
  userStore: new WebStorageStateStore(),
  scope: `openid profile email offline_access api://${appConfig.oauth2.resourceServerClientId}/user_impersonation`,
  loadUserInfo: false
})

Auth.events.addUserSignedIn (() => {
  const oidcUser = useOidcStore();
  Auth.getUser().then( u => {
    if ( u )
      oidcUser.user = u
      console.info("### addUserSignedIn: setted")
    }
  ).catch( err => {
    console.error("### addUserSignedIn: error ", err)
  });
});

Auth.events.addUserLoaded(()=>{
  const oidcUser = useOidcStore();
  Auth.getUser().then( u => {
    if ( u )
      oidcUser.user = u
      console.info("### addUserLoaded")
    }
  ).catch( err => {
    console.error("### addUserLoaded: error ", err)
  });
})

Auth.events.addUserSignedOut(()=>{
  const oidcUser = useOidcStore();
  oidcUser.reset();
})

Auth.events.addAccessTokenExpired(()=>{
  const oidcUser = useOidcStore();
  oidcUser.reset();
})

Auth.events.addSilentRenewError(()=>{
  const oidcUser = useOidcStore();
  oidcUser.reset();
})

export default  Auth
