package com.hotelRoom.allocator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "currency")
@Configuration
public class CurrencyConfig {
    private String code;
}
