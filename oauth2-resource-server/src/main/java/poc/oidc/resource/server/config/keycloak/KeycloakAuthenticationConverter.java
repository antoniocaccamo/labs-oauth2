package poc.oidc.resource.server.config.keycloak;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Profile("keycloak")
@Component
public class KeycloakAuthenticationConverter  implements Converter<Jwt, AbstractAuthenticationToken> {
  public KeycloakAuthenticationConverter() {
  }

  /**
   * Extract roles
   * ...
   * <code>
   *   "realm_access": {
   *     "roles": [
   *       "default-roles-poc",
   *       "offline_access",
   *       "uma_authorization"
   *     ]
   *   },
   *   "resource_access": {
   *     "lab-backend-api": {
   *       "roles": [
   *         "lab-api-editors"
   *       ]
   *     },
   *     "lab-frontend-app": {
   *       "roles": [
   *         "editor"
   *       ]
   *     },
   *     "account": {
   *       "roles": [
   *         "manage-account",
   *         "manage-account-links",   new SimpleGrantedAuthority("ROLE_"+entry.getValue())).collect(Collectors.toSet())
   *         "view-profile"
   *       ]
   *     }
   *   }
   *   ....
   * </code>
   * @param jwt
   * @return
   */
  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {

    Set<SimpleGrantedAuthority> authorities = null;

    Map<String, Object> resourceAccess  = jwt.getClaimAsMap(Keycloak.RESOURCE_ACCESS);
    if (resourceAccess != null && ! resourceAccess.values().isEmpty()) {
      // @formatter:off
      authorities = resourceAccess.values().stream()
                .map(o -> (Map<String, List<String>>)o)
                .flatMap( hm -> hm.values().stream() )
                .flatMap( l -> l.stream() ).map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
      // @formatter:on
    }else {
      authorities = Set.of();
    }
    String principalName = StringUtils.isNoneEmpty(jwt.getClaimAsString(Keycloak.PREFERRED_USERNAME) ) ? jwt.getClaimAsString(Keycloak.PREFERRED_USERNAME) : jwt.getSubject();
    return new JwtAuthenticationToken(jwt, authorities, principalName);
  }
}



interface Keycloak {
  String PREFERRED_USERNAME = "preferred_username";
  String RESOURCE_ACCESS = "resource_access";

}