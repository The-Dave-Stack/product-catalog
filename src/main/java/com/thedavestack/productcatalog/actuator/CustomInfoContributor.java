package com.thedavestack.productcatalog.actuator;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import com.thedavestack.productcatalog.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomInfoContributor implements InfoContributor {

    private final ProductRepository productRepository;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> productCatalogInfo = new HashMap<>();
        
        // Application info
        productCatalogInfo.put("name", "Product Catalog Service");
        productCatalogInfo.put("version", "1.0.0-ENHANCED");
        productCatalogInfo.put("description", "Enhanced Spring Boot REST API for product catalog management");
        productCatalogInfo.put("lastUpdated", Instant.now().toString());
        
        // Runtime stats
        long totalProducts = productRepository.count();
        productCatalogInfo.put("totalProducts", totalProducts);
        
        // Features
        Map<String, Boolean> features = new HashMap<>();
        features.put("jwtAuthentication", true);
        features.put("auditLogging", true);
        features.put("softDelete", true);
        features.put("pagination", true);
        features.put("filtering", true);
        features.put("validation", true);
        productCatalogInfo.put("features", features);
        
        builder.withDetail("productCatalog", productCatalogInfo);
    }
}