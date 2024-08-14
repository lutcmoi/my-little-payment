package org.bdabos.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MyLittleExchangeProperties.class})
public class MyLittleExchangeConfiguration {
}
