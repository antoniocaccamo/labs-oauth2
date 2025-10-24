package labs.oauth2.client.cert.config;

/**
 * @auhtor antonio.caccamo on 2024-05-09 @ 12:26
 */

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class Configuration {

   private String tenantId;

    private String authority;

    private String clientId;

}
