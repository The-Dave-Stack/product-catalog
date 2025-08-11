/**
 * ProductService.java
 *
 * <p>Purpose: - Implements business logic for product catalog operations - Orchestrates
 * ProductRepository operations with domain-specific rules - Handles SKU auto-generation and
 * validation - Provides clean separation between REST layer and data access
 *
 * <p>Logic Overview: 1. Auto-generates SKUs in format '[NNN]-######' when not provided 2. Validates
 * existing SKUs against required format 3. Ensures SKU uniqueness through repository checks 4.
 * Handles not-found scenarios with custom exceptions 5. Maintains reactive patterns with
 * CompletableFuture operations
 *
 * <p>Last Updated: 2025-08-10 by Claude (Model: claude-sonnet-4, Task: task-9 - Implement
 * ProductService business logic)
 */
package com.thedavestack.productcatalog.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductService {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]{3}-\\d{6}$");
    private static final String[] CATEGORY_PREFIXES = {
        "ELC", "CLO", "BOO", "TOY", "HOM", "SPO", "AUT", "GAR", "HEA", "MUS"
    };

    @Inject private ProductRepository productRepository;

    /**
     * Creates a new product with business logic validation and SKU generation.
     *
     * @param product the product to create
     * @return CompletableFuture containing the created product with generated fields
     * @throws DuplicateSkuException if SKU already exists
     * @throws IllegalArgumentException if product data is invalid
     */
    public CompletableFuture<Product> createProduct(Product product) {
        validateProductForCreation(product);

        return generateSkuIfNeeded(product)
                .thenCompose(this::validateSkuUniqueness)
                .thenCompose(
                        productWithSku -> {
                            Product productToCreate =
                                    new Product(
                                            UUID.randomUUID().toString(),
                                            productWithSku.sku(),
                                            productWithSku.name(),
                                            productWithSku.description(),
                                            productWithSku.price(),
                                            productWithSku.category(),
                                            Instant.now(),
                                            Instant.now());
                            return productRepository.create(productToCreate);
                        });
    }

    /**
     * Creates multiple products in a single transaction with business logic validation.
     *
     * @param products the list of products to create
     * @return CompletableFuture containing the list of created products
     * @throws DuplicateSkuException if any SKU already exists
     * @throws IllegalArgumentException if any product data is invalid
     */
    public CompletableFuture<List<Product>> createProductsBatch(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products list cannot be null or empty");
        }

        products.forEach(this::validateProductForCreation);

        return CompletableFuture.supplyAsync(() -> products)
                .thenCompose(this::generateSkusForBatch)
                .thenCompose(this::validateBatchSkuUniqueness)
                .thenCompose(
                        productsWithSkus -> {
                            Instant now = Instant.now();
                            List<Product> productsToCreate =
                                    productsWithSkus.stream()
                                            .map(
                                                    p ->
                                                            new Product(
                                                                    UUID.randomUUID().toString(),
                                                                    p.sku(),
                                                                    p.name(),
                                                                    p.description(),
                                                                    p.price(),
                                                                    p.category(),
                                                                    now,
                                                                    now))
                                            .toList();
                            return productRepository.createBatch(productsToCreate);
                        });
    }

    /**
     * Finds a product by its ID.
     *
     * @param id the product ID
     * @return CompletableFuture containing the product
     * @throws ProductNotFoundException if product is not found
     */
    public CompletableFuture<Product> getProductById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Product ID cannot be null or empty"));
        }

        return productRepository
                .findById(id)
                .thenCompose(
                        optional -> {
                            if (optional.isPresent()) {
                                return CompletableFuture.completedFuture(optional.get());
                            } else {
                                return CompletableFuture.failedFuture(
                                        new ProductNotFoundException(
                                                "Product not found with ID: " + id));
                            }
                        });
    }

    /**
     * Finds a product by its SKU.
     *
     * @param sku the product SKU
     * @return CompletableFuture containing the product
     * @throws ProductNotFoundException if product is not found
     */
    public CompletableFuture<Product> getProductBySku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Product SKU cannot be null or empty"));
        }

        return productRepository
                .findBySku(sku)
                .thenCompose(
                        optional -> {
                            if (optional.isPresent()) {
                                return CompletableFuture.completedFuture(optional.get());
                            } else {
                                return CompletableFuture.failedFuture(
                                        new ProductNotFoundException(
                                                "Product not found with SKU: " + sku));
                            }
                        });
    }

    /**
     * Retrieves all products with pagination support.
     *
     * @param offset the number of records to skip
     * @param limit the maximum number of records to return
     * @return CompletableFuture containing the list of products
     */
    public CompletableFuture<List<Product>> getAllProducts(int offset, int limit) {
        validatePaginationParameters(offset, limit);
        return productRepository.findAll(offset, limit);
    }

    /**
     * Retrieves products by category with pagination support.
     *
     * @param category the product category
     * @param offset the number of records to skip
     * @param limit the maximum number of records to return
     * @return CompletableFuture containing the list of products in the category
     */
    public CompletableFuture<List<Product>> getProductsByCategory(
            String category, int offset, int limit) {
        if (category == null || category.trim().isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Category cannot be null or empty"));
        }
        validatePaginationParameters(offset, limit);
        return productRepository.findByCategory(category, offset, limit);
    }

    /**
     * Updates an existing product with business logic validation.
     *
     * @param product the product with updated information
     * @return CompletableFuture containing the updated product
     * @throws ProductNotFoundException if product is not found
     * @throws DuplicateSkuException if SKU already exists for another product
     */
    public CompletableFuture<Product> updateProduct(Product product) {
        if (product == null || product.id() == null || product.id().trim().isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Product and ID cannot be null or empty"));
        }

        validateProductForUpdate(product);

        return productRepository
                .findById(product.id())
                .thenCompose(
                        optional -> {
                            if (optional.isEmpty()) {
                                return CompletableFuture.failedFuture(
                                        new ProductNotFoundException(
                                                "Product not found with ID: " + product.id()));
                            }

                            Product existingProduct = optional.get();
                            return validateSkuForUpdate(product, existingProduct)
                                    .thenCompose(
                                            validatedProduct -> {
                                                Product updatedProduct =
                                                        new Product(
                                                                validatedProduct.id(),
                                                                validatedProduct.sku(),
                                                                validatedProduct.name(),
                                                                validatedProduct.description(),
                                                                validatedProduct.price(),
                                                                validatedProduct.category(),
                                                                existingProduct.createdAt(),
                                                                Instant.now());
                                                return productRepository.update(updatedProduct);
                                            });
                        });
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the product ID
     * @return CompletableFuture containing true if deleted, false if not found
     * @throws ProductNotFoundException if product is not found
     */
    public CompletableFuture<Boolean> deleteProduct(String id) {
        if (id == null || id.trim().isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Product ID cannot be null or empty"));
        }

        return productRepository
                .deleteById(id)
                .thenCompose(
                        deleted -> {
                            if (deleted) {
                                return CompletableFuture.completedFuture(true);
                            } else {
                                return CompletableFuture.failedFuture(
                                        new ProductNotFoundException(
                                                "Product not found with ID: " + id));
                            }
                        });
    }

    /**
     * Counts the total number of products.
     *
     * @return CompletableFuture containing the total count
     */
    public CompletableFuture<Long> countProducts() {
        return productRepository.count();
    }

    /**
     * Counts the number of products in a specific category.
     *
     * @param category the product category
     * @return CompletableFuture containing the count for the category
     */
    public CompletableFuture<Long> countProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Category cannot be null or empty"));
        }
        return productRepository.countByCategory(category);
    }

    // Private helper methods

    private void validateProductForCreation(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.name() == null || product.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.price() == null || product.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be null or negative");
        }
        if (product.sku() != null
                && !product.sku().trim().isEmpty()
                && !isValidSkuFormat(product.sku())) {
            throw new IllegalArgumentException("Invalid SKU format. Expected: [NNN]-######");
        }
    }

    private void validateProductForUpdate(Product product) {
        validateProductForCreation(product);
    }

    private void validatePaginationParameters(int offset, int limit) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }
        if (limit <= 0 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
    }

    private boolean isValidSkuFormat(String sku) {
        return sku != null && SKU_PATTERN.matcher(sku).matches();
    }

    private CompletableFuture<Product> generateSkuIfNeeded(Product product) {
        if (product.sku() == null || product.sku().trim().isEmpty()) {
            String generatedSku = generateSku(product.category());
            Product productWithSku =
                    new Product(
                            product.id(),
                            generatedSku,
                            product.name(),
                            product.description(),
                            product.price(),
                            product.category(),
                            product.createdAt(),
                            product.updatedAt());
            return CompletableFuture.completedFuture(productWithSku);
        }
        return CompletableFuture.completedFuture(product);
    }

    private String generateSku(String category) {
        String prefix = determineCategoryPrefix(category);
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return prefix + "-" + randomNumber;
    }

    private String determineCategoryPrefix(String category) {
        if (category == null || category.trim().isEmpty()) {
            return CATEGORY_PREFIXES[ThreadLocalRandom.current().nextInt(CATEGORY_PREFIXES.length)];
        }

        String upperCategory = category.toUpperCase().trim();
        return switch (upperCategory) {
            case "ELECTRONICS" -> "ELC";
            case "CLOTHING" -> "CLO";
            case "BOOKS" -> "BOO";
            case "TOYS" -> "TOY";
            case "HOME" -> "HOM";
            case "SPORTS" -> "SPO";
            case "AUTOMOTIVE" -> "AUT";
            case "GARDEN" -> "GAR";
            case "HEALTH" -> "HEA";
            case "MUSIC" -> "MUS";
            default -> upperCategory.length() >= 3
                    ? upperCategory.substring(0, 3)
                    : CATEGORY_PREFIXES[
                            ThreadLocalRandom.current().nextInt(CATEGORY_PREFIXES.length)];
        };
    }

    private CompletableFuture<Product> validateSkuUniqueness(Product product) {
        return productRepository
                .existsBySku(product.sku())
                .thenCompose(
                        exists -> {
                            if (exists) {
                                return CompletableFuture.failedFuture(
                                        new DuplicateSkuException(
                                                "SKU already exists: " + product.sku()));
                            }
                            return CompletableFuture.completedFuture(product);
                        });
    }

    private CompletableFuture<List<Product>> generateSkusForBatch(List<Product> products) {
        List<Product> productsWithSkus =
                products.stream()
                        .map(
                                p -> {
                                    if (p.sku() == null || p.sku().trim().isEmpty()) {
                                        String generatedSku = generateSku(p.category());
                                        return new Product(
                                                p.id(),
                                                generatedSku,
                                                p.name(),
                                                p.description(),
                                                p.price(),
                                                p.category(),
                                                p.createdAt(),
                                                p.updatedAt());
                                    }
                                    return p;
                                })
                        .toList();
        return CompletableFuture.completedFuture(productsWithSkus);
    }

    private CompletableFuture<List<Product>> validateBatchSkuUniqueness(List<Product> products) {
        List<CompletableFuture<Boolean>> validationFutures =
                products.stream().map(p -> productRepository.existsBySku(p.sku())).toList();

        return CompletableFuture.allOf(validationFutures.toArray(new CompletableFuture[0]))
                .thenCompose(
                        ignored -> {
                            for (int i = 0; i < validationFutures.size(); i++) {
                                if (validationFutures.get(i).join()) {
                                    return CompletableFuture.failedFuture(
                                            new DuplicateSkuException(
                                                    "SKU already exists: "
                                                            + products.get(i).sku()));
                                }
                            }
                            return CompletableFuture.completedFuture(products);
                        });
    }

    private CompletableFuture<Product> validateSkuForUpdate(
            Product updatedProduct, Product existingProduct) {
        if (updatedProduct.sku().equals(existingProduct.sku())) {
            return CompletableFuture.completedFuture(updatedProduct);
        }

        return productRepository
                .existsBySku(updatedProduct.sku())
                .thenCompose(
                        exists -> {
                            if (exists) {
                                return CompletableFuture.failedFuture(
                                        new DuplicateSkuException(
                                                "SKU already exists: " + updatedProduct.sku()));
                            }
                            return CompletableFuture.completedFuture(updatedProduct);
                        });
    }
}
