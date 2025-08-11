package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.json.bind.annotation.JsonbProperty;

public record ProductResponse(
        @JsonbProperty("id") String id,
        @JsonbProperty("sku") String sku,
        @JsonbProperty("name") String name,
        @JsonbProperty("description") String description,
        @JsonbProperty("price") BigDecimal price,
        @JsonbProperty("category") String category,
        @JsonbProperty("createdAt") Instant createdAt,
        @JsonbProperty("updatedAt") Instant updatedAt) {}
