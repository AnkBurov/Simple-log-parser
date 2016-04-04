package ru.cti.simplelogparcer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;


/**
 * Spring Java configuration file
 */
@org.springframework.context.annotation.Configuration
@PropertySources({@PropertySource(value = "file:etc/config.properties")})
public class Configuration {
    @Autowired
    Environment env;

    @Bean
    public Main main() {
        return new Main(env.getProperty("logDirPath"),
                env.getProperty("logFileExtension"),
                env.getProperty("foundLogPath"),
                env.getProperty("callIdRegexp"),
                env.getProperty("soughtError"));
    }
}
