/**
 * ProductService.java
 *
 * <p>Design Doc: ./docs/API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Handles the business logic for product management. - Orchestrates data operations
 * by interacting with the ProductRepository.
 *
 * <p>Logic Overview: - Provides methods for all CRUD (Create, Read, Update, Delete) operations. -
 * Implements SKU generation and validation for new products. - Ensures data integrity for batch
 * operations using transactions.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: Initial creation for task-9)
 */
package com.thedavestack.productcatalog.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thedavestack.productcatalog.exception.ProductNotFoundException;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products.
     *
     * @return a list of all products.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return an Optional containing the product if found, or empty otherwise.
     */
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Creates a new product. A unique SKU is generated for the product before saving.
     *
     * @param product the product to create.
     * @return the created product.
     */
    @Transactional
    public Product createProduct(Product product) {
        // Generate a unique SKU
        product.setSku(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    /**
     * Creates multiple products in a single transaction.
     *
     * @param products the list of products to create.
     * @return the list of created products.
     */
    @Transactional
    public List<Product> createMultipleProducts(List<Product> products) {
        return products.stream().map(this::createProduct).collect(Collectors.toList());
    }

    /**
     * Updates an existing product.
     *
     * @param id the ID of the product to update.
     * @param productDetails the new details for the product.
     * @return the updated product.
     * @throws ProductNotFoundException if the product with the given ID is not found.
     */
    @Transactional
    public Product updateProduct(String id, Product productDetails) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());

        return productRepository.save(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete.
     * @throws ProductNotFoundException if the product with the given ID is not found.
     */
    @Transactional
    public void deleteProduct(String id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }
}
