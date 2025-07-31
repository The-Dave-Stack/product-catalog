package com.thedavestack.productcatalog.dto;

import java.math.BigDecimal;

public record ProductResponse(
        String id, String sku, String name, String description, BigDecimal price) {}
