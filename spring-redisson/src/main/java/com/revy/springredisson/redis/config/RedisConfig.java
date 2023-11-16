package com.revy.springredisson.redis.config;


import com.revy.springredisson.redis.properties.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * Created by Revy on 2023.11.13
 * Redis Config
 */

@Configuration
public class RedisConfig {

    @Bean(destroyMethod = "stop")
    public RedisServer redisServer(RedisProperties redisProperties) throws IOException {
        RedisServer redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
        return redisServer;
    }

    @DependsOn("redisServer")
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://%s:%s".formatted(redisProperties.getHost(), redisProperties.getPort()));
        return Redisson.create(config);
    }
}
