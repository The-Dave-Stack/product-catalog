/**
 * ProductControllerIT.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Provides integration tests for the ProductController.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-10)
 */
package com.thedavestack.productcatalog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedavestack.productcatalog.BaseIntegrationTest;
import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProductControllerIT extends BaseIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private ProductRepository productRepository;

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        CreateProductRequest request =
                new CreateProductRequest("Test Product", "Description", new BigDecimal("10.00"));

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(new BigDecimal("10.00"));
        product.setSku("test-sku");
        product = productRepository.save(product);

        mockMvc.perform(get("/api/v1/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/products/{id}", "non-existent-id"))
                .andExpect(status().isNotFound());
    }
}
