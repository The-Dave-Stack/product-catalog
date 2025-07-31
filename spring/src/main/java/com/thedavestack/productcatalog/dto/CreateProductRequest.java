/**
 * CreateProductRequest.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Represents the DTO for creating a new product.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-10)
 */
package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
        @NotBlank(message = "Product name cannot be blank") String name,
        String description,
        @Positive(message = "Product price must be positive") BigDecimal price) {}
