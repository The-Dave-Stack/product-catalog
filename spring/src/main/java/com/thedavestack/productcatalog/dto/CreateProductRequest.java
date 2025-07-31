package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
        @NotBlank(message = "Product name cannot be blank") String name,
        String description,
        @Positive(message = "Product price must be positive") BigDecimal price) {}
