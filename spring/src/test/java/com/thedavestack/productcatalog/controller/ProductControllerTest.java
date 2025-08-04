/**
 * ProductControllerTest.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Provides unit tests for the ProductController.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-10)
 */
package com.thedavestack.productcatalog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.mapper.ProductMapper;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private ProductService productService;

    @MockitoBean private ProductMapper productMapper;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        CreateProductRequest request =
                new CreateProductRequest(
                        "Test Product", "Description", new BigDecimal("10.00"), null);
        Product product = new Product();
        product.setId("1");
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(new BigDecimal("10.00"));

        ProductResponse response =
                new ProductResponse(
                        "1", "sku", "Test Product", "Description", new BigDecimal("10.00"));

        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(product);
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {
        Product product = new Product();
        product.setId("1");
        product.setName("Test Product");

        ProductResponse response =
                new ProductResponse(
                        "1", "sku", "Test Product", "Description", new BigDecimal("10.00"));

        when(productService.findById("1")).thenReturn(Optional.of(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_shouldReturnNotFound() throws Exception {
        when(productService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/1")).andExpect(status().isNotFound());
    }
}
