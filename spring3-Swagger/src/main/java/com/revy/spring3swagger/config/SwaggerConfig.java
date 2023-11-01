package com.revy.spring3swagger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Swagger Config Example",
                description = "Swagger Config Example"
//                ,version = "v1"
        ))
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/**"};


        return GroupedOpenApi.builder()
                .group("Swagger Config Example")
                .pathsToMatch(paths)
                .build();
    }
}
