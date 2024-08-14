package org.bdabos.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "my-little-exchange.exchange-service")
public class MyLittleExchangeProperties {
    @NotNull
    @NotEmpty
    private String baseUrl;
}
