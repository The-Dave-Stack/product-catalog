/**
 * OpenApiConfig.java
 *
 * Purpose:
 * - Configures Springdoc OpenAPI for the Product Catalog API.
 * - Provides custom API information such as title, version, and description.
 *
 * Logic Overview:
 * 1. Defines a Spring @Configuration class.
 * 2. Creates a @Bean for OpenAPI to customize the API documentation.
 *
 * Last Updated:
 * 2025-08-05 by Cline (Model: claude-3-opus, Task: Added Swagger configuration)
 */
package com.thedavestack.productcatalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Catalog API")
                        .version("1.0")
                        .description("A RESTful API for managing a product catalog."));
    }
}
