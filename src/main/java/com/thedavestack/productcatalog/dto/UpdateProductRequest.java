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

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProductRequest {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotBlank(message = "Product description cannot be blank")
    private String description;

    @NotNull(message = "Product price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0")
    private BigDecimal price;
}
