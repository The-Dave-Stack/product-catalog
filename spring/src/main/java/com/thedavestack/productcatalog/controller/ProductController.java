package com.thedavestack.productcatalog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
