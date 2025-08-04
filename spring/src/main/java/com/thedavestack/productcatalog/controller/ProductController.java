/**
 * ProductController.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Handles HTTP requests related to product accounts. - GET /products/:id → fetch
 * product by ID - POST /products → create a new product
 *
 * <p>Logic Overview: 1. Validate request payloads and parameters. 2. Call the corresponding
 * ProductService method to interact with the database. 3. Handle errors using a centralized error
 * handler. 4. Send appropriate HTTP status codes and JSON responses.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-10)
 */
package com.thedavestack.productcatalog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.exception.ProductNotFoundException;
import com.thedavestack.productcatalog.mapper.ProductMapper;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestBody @Valid CreateProductRequest createProductRequest) {
        Product product = productMapper.toEntity(createProductRequest);
        Product createdProduct = productService.createProduct(product);
        return productMapper.toResponse(createdProduct);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        Product product =
                productService.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }
}
