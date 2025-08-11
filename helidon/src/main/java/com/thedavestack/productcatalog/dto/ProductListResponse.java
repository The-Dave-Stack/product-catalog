package com.thedavestack.productcatalog.dto;

import java.util.List;

import jakarta.json.bind.annotation.JsonbProperty;

public record ProductListResponse(
        @JsonbProperty("products") List<ProductResponse> products,
        @JsonbProperty("page") int page,
        @JsonbProperty("size") int size,
        @JsonbProperty("totalElements") long totalElements,
        @JsonbProperty("totalPages") int totalPages) {}
