import { UserManager, WebStorageStateStore , Log} from 'oidc-client-ts'
import { useOidcUser } from '@/stores/oidc';

Log.setLevel(Log.WARN)
Log.setLogger(console);

export const kc = {
//  applicationUrl: "http://localhost:3000", 
  authorityUrl: 'https://login.private.antoniocaccamo.work',
  realm       : 'labs',
  clientId    : 'lab-frontend-app'
}


const Auth = new UserManager({
  authority: `${kc.authorityUrl}/realms/${kc.realm}`,
  // metadata :  {
  //   authorization_endpoint : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/auth`,
  //   token_endpoint         : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/token`,
  //   userinfo_endpoint      : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/userinfo`,
  //   issuer                 : `${kc.authorityUrl}/realms/${kc.realm}`,
  //   jwks_uri               : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/certs`
  // }, 
  client_id: kc.clientId,
  redirect_uri: `${window.location.origin}/auth/callback`,
  silent_redirect_uri: `${window.location.origin}/silent-refresh`,
  post_logout_redirect_uri: `${window.location.origin}`,
  response_type: 'code',
  response_mode: 'fragment',
  userStore: new WebStorageStateStore(),
  loadUserInfo: true
})

Auth.events.addUserSignedIn (() => {
  const oidcUser = useOidcUser();
  Auth.getUser().then( u => {
    if ( u ) 
      oidcUser.user = u 
      console.info("### addUserSignedIn: setted")
    }  
  )
});

Auth.events.addUserLoaded(()=>{
  const oidcUser = useOidcUser();
  Auth.getUser().then( u => {
    if ( u ) 
      oidcUser.user = u 
      console.info("### addUserLoaded")
    }  
  )
})

Auth.events.addUserSignedOut(()=>{
  const oidcUser = useOidcUser();
  oidcUser.reset();
})

Auth.events.addAccessTokenExpired(()=>{
  const oidcUser = useOidcUser();
  oidcUser.reset();
})

Auth.events.addSilentRenewError(()=>{
  const oidcUser = useOidcUser();
  oidcUser.reset();
})

console.info("Auth.settings.authority: " + Auth.settings.authority);

export default  Auth