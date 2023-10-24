package com.revy.springjwt.infra.web;

import com.revy.springjwt.common.res.ErrorResponse;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Revy"
                ),
                title = "JWT 샘플",
                description = "JWT 샘플"),
        servers = {
                @Server(
                        description = "Development",
                        url = "http://localhost:8081"

                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
        @Bean
        public OpenApiCustomizer schemaCustomizer() {
                ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class));
                return openApi -> openApi
                        .schema(resolvedSchema.schema.getName(), resolvedSchema.schema);
        }
}
