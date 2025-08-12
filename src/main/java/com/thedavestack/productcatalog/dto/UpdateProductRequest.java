/**
 * UpdateProductRequest.java
 *
 * <p>Purpose: - DTO for updating an existing product. - Contains fields that can be modified for a
 * product.
 *
 * <p>Logic Overview: 1. Uses Lombok annotations for boilerplate code reduction. 2. Includes
 * validation annotations for input constraints.
 *
 * <p>Last Updated: 2025-08-05 by Cline (Model: claude-3-opus, Task: Created UpdateProductRequest
 * DTO)
 */
package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;

import com.thedavestack.productcatalog.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProductRequest {

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be positive")
    private BigDecimal price;

    private Category category;

    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity;

    @PositiveOrZero(message = "Minimum stock level must be zero or positive")
    private Integer minStockLevel;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    @Positive(message = "Weight must be positive")
    private BigDecimal weight;

    @Size(max = 255, message = "Dimensions cannot exceed 255 characters")
    private String dimensions;

    private Boolean active;
}
