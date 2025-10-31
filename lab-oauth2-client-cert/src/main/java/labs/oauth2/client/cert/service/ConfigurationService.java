package labs.oauth2.client.cert.service;

import labs.oauth2.client.cert.config.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

/**
 * @auhtor antonio.caccamo on 2024-05-09 @ 12:39
 */
@Slf4j
public class ConfigurationService {

    private static final String CONFIG_FILE     = "configuration.yaml";


    @Getter
    private final Configuration configuration;
    private final Yaml yaml;


    public ConfigurationService() throws FileNotFoundException {
        this.yaml = new Yaml( new Constructor(Configuration.class, new LoaderOptions()));
        Path path = Path.of(CONFIG_FILE);

        log.info("config {}", path.toAbsolutePath().toFile().getAbsolutePath());

        this.configuration = yaml.load(new FileReader(path.toAbsolutePath().toFile().getAbsolutePath()));
    }
}
