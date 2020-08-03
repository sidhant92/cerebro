package com.ant.search.cerebro.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 02/08/2020
 */
@Configuration
@Getter
@Setter
public class InMemoryCacheConfiguration {
    @Value ("${cache.memory.maxsize}")
    private Integer cacheMaxSize;

    @Value ("${cache.memory.expiry_after_write_minutes}")
    private Integer expiryAfterWriteInMinutes;

    @Value ("${elastic.refresh_after_write_minutes}")
    private Integer refreshAfterWriteInMinutes;

    @Bean
    public InMemoryCacheConfiguration getCacheConfig() {
        return this;
    }
}
