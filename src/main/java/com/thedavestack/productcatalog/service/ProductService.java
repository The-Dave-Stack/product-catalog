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
 * <p>Last Updated: 2025-08-05 by Cline (Model: claude-3-opus, Task: Added update and delete product
 * methods)
 */
package com.thedavestack.productcatalog.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thedavestack.productcatalog.exception.DuplicateSkuException;
import com.thedavestack.productcatalog.exception.ProductNotFoundException;
import com.thedavestack.productcatalog.model.AuditLog;
import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AuditService auditService;

    public ProductService(ProductRepository productRepository, AuditService auditService) {
        this.productRepository = productRepository;
        this.auditService = auditService;
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
     * Creates a new product. If no SKU is provided, a unique SKU is generated. If a SKU is
     * provided, it must be unique.
     *
     * @param product the product to create.
     * @return the created product.
     * @throws DuplicateSkuException if the provided SKU already exists.
     */
    @Transactional
    public Product createProduct(Product product) {
        // If no SKU is provided, generate a unique one
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            product.setSku(UUID.randomUUID().toString());
        } else {
            // If SKU is provided, check for duplicates
            if (productRepository.existsBySku(product.getSku())) {
                throw new DuplicateSkuException(product.getSku());
            }
        }
        Product savedProduct = productRepository.save(product);

        // Audit log
        auditService.logAction(
                "Product", savedProduct.getId(), AuditLog.AuditAction.CREATE, null, savedProduct);

        return savedProduct;
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

        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        if (productDetails.getStockQuantity() != null) {
            product.setStockQuantity(productDetails.getStockQuantity());
        }
        if (productDetails.getMinStockLevel() != null) {
            product.setMinStockLevel(productDetails.getMinStockLevel());
        }
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }
        if (productDetails.getWeight() != null) {
            product.setWeight(productDetails.getWeight());
        }
        if (productDetails.getDimensions() != null) {
            product.setDimensions(productDetails.getDimensions());
        }
        if (productDetails.getActive() != null) {
            product.setActive(productDetails.getActive());
        }

        Product savedProduct = productRepository.save(product);

        // Audit log
        auditService.logAction(
                "Product",
                savedProduct.getId(),
                AuditLog.AuditAction.UPDATE,
                product,
                savedProduct);

        return savedProduct;
    }

    /**
     * Deletes a product by its ID (soft delete).
     *
     * @param id the ID of the product to delete.
     * @throws ProductNotFoundException if the product with the given ID is not found.
     */
    @Transactional
    public void deleteProduct(String id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        // Audit log before deletion
        auditService.logAction(
                "Product", product.getId(), AuditLog.AuditAction.DELETE, product, null);

        productRepository.delete(product);
    }

    /**
     * Retrieves all products with pagination.
     *
     * @param pageable the pagination information.
     * @return a page of products.
     */
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Retrieves products with filtering and pagination.
     *
     * @param name optional name filter.
     * @param category optional category filter.
     * @param active optional active status filter.
     * @param pageable the pagination information.
     * @return a page of products.
     */
    public Page<Product> findWithFilters(
            String name, Category category, Boolean active, Pageable pageable) {
        return productRepository.findWithFilters(name, category, active, pageable);
    }

    /**
     * Retrieves products with low stock.
     *
     * @param pageable the pagination information.
     * @return a page of products with low stock.
     */
    public Page<Product> findLowStockProducts(Pageable pageable) {
        return productRepository.findLowStockProducts(pageable);
    }
}
