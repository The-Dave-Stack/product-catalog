/**
 * ProductResponse.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Represents the DTO for a product response.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-10)
 */
package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.thedavestack.productcatalog.model.Category;

public record ProductResponse(
        String id, 
        String sku, 
        String name, 
        String description, 
        BigDecimal price,
        Category category,
        Integer stockQuantity,
        Integer minStockLevel,
        String imageUrl,
        BigDecimal weight,
        String dimensions,
        Boolean active,
        Instant createdAt,
        Instant updatedAt,
        Long version) {}
