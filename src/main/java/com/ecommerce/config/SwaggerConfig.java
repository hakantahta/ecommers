package com.ecommerce.config;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.webmvc.api.OpenApiResource;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiResource openApiResource() {
        return new OpenApiResource();
    }
} 