package com.thedavestack.productcatalog;

import com.thedavestack.productcatalog.service.ProductService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductCatalogSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogSpringApplication.class, args);
    }

    @Bean
    ToolCallbackProvider productCatalogTools(ProductService productService) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(productService)
                .build();
    }

}
