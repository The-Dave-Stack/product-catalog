/**
 * Product.java
 *
 * <p>Design Doc: ./docs/api-design-product-catalog-helidon.md
 *
 * <p>Purpose: - Defines the data structure for a product in the catalog. - Serves as a plain data
 * carrier (POJO/Record) without persistence annotations.
 *
 * <p>Logic Overview: - A Java record to encapsulate product attributes.
 *
 * <p>Last Updated: 2025-08-06 by Cline (Model: claude-3-opus, Task: Implemented Product data
 * object)
 */
package com.thedavestack.productcatalog.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.thedavestack.productcatalog.dto.CreateProductRequest;

public record Product(
        String id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String category,
        Instant createdAt,
        Instant updatedAt) {

    public static Product forCreation(
            String sku, String name, String description, BigDecimal price, String category) {
        return new Product(null, sku, name, description, price, category, null, null);
    }

    public static Product forUpdate(
            String id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            String category) {
        return new Product(id, sku, name, description, price, category, null, null);
    }

    public static Product fromRequest(CreateProductRequest request) {
        return forCreation(
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.category());
    }

    public static Product fromRequestForUpdate(String id, CreateProductRequest request) {
        return forUpdate(
                id,
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.category());
    }
}
