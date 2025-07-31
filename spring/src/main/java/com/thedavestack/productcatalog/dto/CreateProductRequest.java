package com.thedavestack.productcatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateProductRequest(
    @NotBlank(message = "Product name cannot be blank") String name,
    String description,
    @Positive(message = "Product price must be positive") BigDecimal price) {}
