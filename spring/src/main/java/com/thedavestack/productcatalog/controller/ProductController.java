/**
 * ProductController.java
 *
 * Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * Purpose:
 * - Handles HTTP requests related to product accounts.
 * - GET /products/:id → fetch product by ID
 * - POST /products → create a new product
 *
 * Logic Overview:
 * 1. Validate request payloads and parameters.
 * 2. Call the corresponding ProductService method to interact with the database.
 * 3. Handle errors using a centralized error handler.
 * 4. Send appropriate HTTP status codes and JSON responses.
 *
 * Last Updated:
 * 2025-08-05 by Cline (Model: claude-3-opus, Task: Added Swagger annotations)
 */
package com.thedavestack.productcatalog.controller;

import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.exception.ProductNotFoundException;
import com.thedavestack.productcatalog.mapper.ProductMapper;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(
            summary = "Create a new product",
            description = "Create a new product by providing its details.",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Product created successfully",
                        content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "409", description = "Product with given SKU already exists")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestBody @Valid CreateProductRequest createProductRequest) {
        Product product = productMapper.toEntity(createProductRequest);
        Product createdProduct = productService.createProduct(product);
        return productMapper.toResponse(createdProduct);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve a product by its unique identifier.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Product found",
                        content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        Product product =
                productService.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }
}
