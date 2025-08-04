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

public record ProductResponse(
        String id, String sku, String name, String description, BigDecimal price) {}
