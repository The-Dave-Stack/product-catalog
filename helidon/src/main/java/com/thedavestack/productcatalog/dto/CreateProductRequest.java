package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
        @JsonbProperty("sku") String sku,
        @JsonbProperty("name") @NotBlank(message = "Product name is required") String name,
        @JsonbProperty("description") String description,
        @JsonbProperty("price")
                @NotNull(message = "Product price is required")
                @DecimalMin(
                        value = "0.0",
                        inclusive = false,
                        message = "Price must be greater than zero")
                BigDecimal price,
        @JsonbProperty("category") String category) {}
