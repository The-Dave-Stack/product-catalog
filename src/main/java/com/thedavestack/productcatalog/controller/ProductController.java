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
 * <p>Last Updated: 2025-08-05 by Cline (Model: claude-3-opus, Task: Added PUT and DELETE endpoints
 * for products)
 */
package com.thedavestack.productcatalog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductPageResponse;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.dto.UpdateProductRequest;
import com.thedavestack.productcatalog.exception.ProductNotFoundException;
import com.thedavestack.productcatalog.mapper.ProductMapper;
import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
                        content =
                                @Content(schema = @Schema(implementation = ProductResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(
                        responseCode = "409",
                        description = "Product with given SKU already exists")
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
            summary = "Get all products",
            description = "Retrieve a paginated list of products with optional filtering.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Products retrieved successfully",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                ProductPageResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid parameters")
            })
    @GetMapping
    public ProductPageResponse getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Boolean active) {

        Sort.Direction direction =
                sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sortBy));

        Page<Product> productPage =
                productService.findWithFilters(name, category, active, pageable);

        return new ProductPageResponse(
                productPage.getContent().stream().map(productMapper::toResponse).toList(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast(),
                productPage.hasNext(),
                productPage.hasPrevious());
    }

    @Operation(
            summary = "Get low stock products",
            description = "Retrieve products with stock quantity at or below minimum stock level.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Low stock products retrieved successfully",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                ProductPageResponse.class)))
            })
    @GetMapping("/low-stock")
    public ProductPageResponse getLowStockProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "stockQuantity") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort.Direction direction =
                sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sortBy));

        Page<Product> productPage = productService.findLowStockProducts(pageable);

        return new ProductPageResponse(
                productPage.getContent().stream().map(productMapper::toResponse).toList(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast(),
                productPage.hasNext(),
                productPage.hasPrevious());
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve a product by its unique identifier.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Product found",
                        content =
                                @Content(schema = @Schema(implementation = ProductResponse.class))),
                @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        Product product =
                productService.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }

    @Operation(
            summary = "Update an existing product",
            description = "Update an existing product by its ID.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Product updated successfully",
                        content =
                                @Content(schema = @Schema(implementation = ProductResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable String id,
            @RequestBody @Valid UpdateProductRequest updateProductRequest) {
        Product productDetails = productMapper.toEntity(updateProductRequest);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return productMapper.toResponse(updatedProduct);
    }

    @Operation(
            summary = "Delete a product by ID",
            description = "Delete a product by its unique identifier.",
            responses = {
                @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
