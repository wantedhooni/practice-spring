package com.revy.springredisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringRedissonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedissonApplication.class, args);
    }

}
