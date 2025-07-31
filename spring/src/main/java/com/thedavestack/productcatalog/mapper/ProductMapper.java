/**
 * ProductMapper.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Maps between Product entities and DTOs.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-10)
 */
package com.thedavestack.productcatalog.mapper;

import org.springframework.stereotype.Component;

import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.model.Product;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest createProductRequest) {
        Product product = new Product();
        product.setName(createProductRequest.name());
        product.setDescription(createProductRequest.description());
        product.setPrice(createProductRequest.price());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }
}
