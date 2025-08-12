/**
 * ProductMapper.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Maps between Product entities and DTOs.
 *
 * <p>Last Updated: 2025-08-05 by Cline (Model: claude-3-opus, Task: Added mapping for
 * UpdateProductRequest DTO)
 */
package com.thedavestack.productcatalog.mapper;

import org.springframework.stereotype.Component;

import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.dto.UpdateProductRequest;
import com.thedavestack.productcatalog.model.Product;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest createProductRequest) {
        Product product = new Product();
        product.setName(createProductRequest.name());
        product.setDescription(createProductRequest.description());
        product.setPrice(createProductRequest.price());
        product.setSku(createProductRequest.sku());
        return product;
    }

    public Product toEntity(UpdateProductRequest updateProductRequest) {
        Product product = new Product();
        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());
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
