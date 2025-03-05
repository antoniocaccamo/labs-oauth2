import { UserManager, WebStorageStateStore , Log} from 'oidc-client-ts'

Log.setLevel(Log.DEBUG)
Log.setLogger(console);

export const kc = {
  applicationUrl: "http://localhost:3000",
  authorityUrl: 'https://login.private.antoniocaccamo.work',
  realm       : 'labs',
  clientId    : 'lab-frontend-app'
}


const Auth = new UserManager({
  authority: `${kc.authorityUrl}/realms/${kc.realm}`,
  metadata :  {
    authorization_endpoint : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/auth`,
    token_endpoint         : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/token`,
    userinfo_endpoint      : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/userinfo`,
    issuer                 : `${kc.authorityUrl}/realms/${kc.realm}`,
    jwks_uri               : `${kc.authorityUrl}/realms/${kc.realm}/protocol/openid-connect/certs`
  }, 
  client_id: kc.clientId,
  redirect_uri: `${window.location.origin}/auth`,
  silent_redirect_uri: `${window.location.origin}/silent-refresh`,
  post_logout_redirect_uri: `${window.location.origin}`,
  response_type: 'code',
  response_mode: 'fragment',
  userStore: new WebStorageStateStore(),
  loadUserInfo: true
})

Auth.events.addUserSignedIn ( function () {
  console.info(" signed in !!!")
});

console.info("Auth.settings.authority: " + Auth.settings.authority);

export default Auth