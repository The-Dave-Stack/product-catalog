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

import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.validation.ValidSku;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank(message = "Name cannot be empty") 
        @Size(max = 255, message = "Name cannot exceed 255 characters")
        String name,
        
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,
        
        @NotNull(message = "Price is required")
        @Positive(message = "Product price must be positive") 
        BigDecimal price,
        
        @ValidSku
        String sku,
        
        Category category,
        
        @PositiveOrZero(message = "Stock quantity must be zero or positive")
        Integer stockQuantity,
        
        @PositiveOrZero(message = "Minimum stock level must be zero or positive")
        Integer minStockLevel,
        
        @Size(max = 500, message = "Image URL cannot exceed 500 characters")
        String imageUrl,
        
        @Positive(message = "Weight must be positive")
        BigDecimal weight,
        
        @Size(max = 255, message = "Dimensions cannot exceed 255 characters")
        String dimensions,
        
        Boolean active) {}
