package com.thedavestack.productcatalog.dto;

import java.util.List;

public record ProductPageResponse(
        List<ProductResponse> products,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious) {}