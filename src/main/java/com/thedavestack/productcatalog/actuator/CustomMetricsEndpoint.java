package com.thedavestack.productcatalog.actuator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuator.endpoint.annotation.Endpoint;
import org.springframework.boot.actuator.endpoint.annotation.ReadOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@Endpoint(id = "product-metrics")
@RequiredArgsConstructor
public class CustomMetricsEndpoint {

    private final ProductRepository productRepository;

    @ReadOperation
    public Map<String, Object> metrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Basic counts
        long totalProducts = productRepository.count();
        long activeProducts = productRepository.findByActive(true, PageRequest.of(0, 1)).getTotalElements();
        long lowStockProducts = productRepository.findLowStockProducts(PageRequest.of(0, 1)).getTotalElements();
        
        metrics.put("totalProducts", totalProducts);
        metrics.put("activeProducts", activeProducts);
        metrics.put("inactiveProducts", totalProducts - activeProducts);
        metrics.put("lowStockProducts", lowStockProducts);
        
        // Category breakdown
        Map<String, Long> categoryBreakdown = new HashMap<>();
        for (Category category : Category.values()) {
            long count = productRepository.findByCategory(category, PageRequest.of(0, 1)).getTotalElements();
            categoryBreakdown.put(category.getDisplayName(), count);
        }
        metrics.put("productsByCategory", categoryBreakdown);
        
        // Calculate average price (simplified - would be more efficient with a native query)
        try {
            BigDecimal averagePrice = productRepository.findAll().stream()
                .filter(p -> p.getActive())
                .map(p -> p.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(activeProducts, 1)), 2, java.math.RoundingMode.HALF_UP);
            metrics.put("averagePrice", averagePrice);
        } catch (Exception e) {
            metrics.put("averagePrice", "Error calculating");
        }
        
        // Stock metrics
        long totalStockQuantity = productRepository.findAll().stream()
            .mapToInt(p -> p.getStockQuantity() != null ? p.getStockQuantity() : 0)
            .sum();
        metrics.put("totalStockQuantity", totalStockQuantity);
        
        return metrics;
    }
}