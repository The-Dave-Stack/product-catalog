/**
 * Product.java
 *
 * <p>Design Doc: ./docs/api-design-product-catalog-helidon.md
 *
 * <p>Purpose: - Defines the data structure for a product in the catalog. - Serves as a plain data
 * carrier (POJO/Record) without persistence annotations.
 *
 * <p>Logic Overview: - A Java record to encapsulate product attributes.
 *
 * <p>Last Updated: 2025-08-06 by Cline (Model: claude-3-opus, Task: Implemented Product data
 * object)
 */
package com.thedavestack.productcatalog.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Product(
        String id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String category,
        Instant createdAt,
        Instant updatedAt) {}
