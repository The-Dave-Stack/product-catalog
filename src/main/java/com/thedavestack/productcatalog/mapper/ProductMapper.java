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
        product.setCategory(createProductRequest.category());
        product.setStockQuantity(
                createProductRequest.stockQuantity() != null
                        ? createProductRequest.stockQuantity()
                        : 0);
        product.setMinStockLevel(
                createProductRequest.minStockLevel() != null
                        ? createProductRequest.minStockLevel()
                        : 0);
        product.setImageUrl(createProductRequest.imageUrl());
        product.setWeight(createProductRequest.weight());
        product.setDimensions(createProductRequest.dimensions());
        product.setActive(
                createProductRequest.active() != null ? createProductRequest.active() : true);
        return product;
    }

    public Product toEntity(UpdateProductRequest updateProductRequest) {
        Product product = new Product();
        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());
        product.setCategory(updateProductRequest.getCategory());
        product.setStockQuantity(updateProductRequest.getStockQuantity());
        product.setMinStockLevel(updateProductRequest.getMinStockLevel());
        product.setImageUrl(updateProductRequest.getImageUrl());
        product.setWeight(updateProductRequest.getWeight());
        product.setDimensions(updateProductRequest.getDimensions());
        product.setActive(updateProductRequest.getActive());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getMinStockLevel(),
                product.getImageUrl(),
                product.getWeight(),
                product.getDimensions(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getVersion());
    }
}
