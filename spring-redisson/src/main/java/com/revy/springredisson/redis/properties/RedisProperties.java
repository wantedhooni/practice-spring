package com.revy.springredisson.redis.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Revy on 2023.11.13
 * Redis Properties
 */

@Getter
@ConfigurationProperties("redis")
public class RedisProperties {
    private final String host;
    private final Integer port;

    public RedisProperties(String host, Integer port) {
        this.host = host;
        this.port = port;
    }
}
