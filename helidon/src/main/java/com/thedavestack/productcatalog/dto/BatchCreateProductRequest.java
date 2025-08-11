package com.thedavestack.productcatalog.dto;

import java.util.List;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BatchCreateProductRequest(
        @JsonbProperty("products")
                @NotNull(message = "Products list cannot be null")
                @NotEmpty(message = "Products list cannot be empty")
                @Valid
                List<CreateProductRequest> products) {}
