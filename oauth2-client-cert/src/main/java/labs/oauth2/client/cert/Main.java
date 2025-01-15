package labs.oauth2.client.cert;

import labs.oauth2.client.cert.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

/**
 * @auhtor antonio.caccamo on 2024-05-09 @ 12:09
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        log.info("config {}", new ConfigurationService().getConfiguration());
    }
}
