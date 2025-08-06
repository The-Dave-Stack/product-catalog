/**
 * ProductRepository.java
 *
 * <p>Design Doc: ./docs/api-design-product-catalog-helidon.md
 *
 * <p>Purpose: - Defines the contract for product data access operations - Abstracts database
 * interactions following Repository pattern - Provides interface for CRUD operations and batch
 * processing
 *
 * <p>Logic Overview: 1. Defines standard CRUD operations (create, read, update, delete) 2. Supports
 * batch operations for performance 3. Provides paginated listing capabilities 4. Returns Optional
 * for single item queries to handle not-found cases 5. Includes generic findBy method with
 * type-safe field enumeration
 *
 * <p>Last Updated: 2025-08-06 by Cline (Model: claude-3-opus, Task: Implementing ProductRepository)
 */
package com.thedavestack.productcatalog.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.thedavestack.productcatalog.model.Product;

public interface ProductRepository {

    /** Enumeration of searchable product fields for type-safe queries. */
    enum ProductField {
        ID("id"),
        SKU("sku"),
        NAME("name"),
        DESCRIPTION("description"),
        CATEGORY("category");

        private final String columnName;

        ProductField(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

    /**
     * Creates a new product in the database.
     *
     * @param product the product to create
     * @return CompletableFuture containing the created product with generated ID and timestamps
     */
    CompletableFuture<Product> create(Product product);

    /**
     * Creates multiple products in a single transaction.
     *
     * @param products the list of products to create
     * @return CompletableFuture containing the list of created products with generated IDs and
     *     timestamps
     */
    CompletableFuture<List<Product>> createBatch(List<Product> products);

    /**
     * Finds a product by its ID.
     *
     * @param id the product ID
     * @return CompletableFuture containing Optional of the product, empty if not found
     */
    CompletableFuture<Optional<Product>> findById(String id);

    /**
     * Finds a product by its SKU.
     *
     * @param sku the product SKU
     * @return CompletableFuture containing Optional of the product, empty if not found
     */
    CompletableFuture<Optional<Product>> findBySku(String sku);

    /**
     * Generic method to find a product by any field.
     *
     * @param field the product field to search by (type-safe enum)
     * @param value the value to search for
     * @return CompletableFuture containing Optional of the product, empty if not found
     */
    CompletableFuture<Optional<Product>> findBy(ProductField field, String value);

    /**
     * Generic method to find multiple products by any field with pagination.
     *
     * @param field the product field to search by (type-safe enum)
     * @param value the value to search for
     * @param offset the number of records to skip
     * @param limit the maximum number of records to return
     * @return CompletableFuture containing the list of matching products
     */
    CompletableFuture<List<Product>> findAllBy(
            ProductField field, String value, int offset, int limit);

    /**
     * Retrieves all products with pagination support.
     *
     * @param offset the number of records to skip
     * @param limit the maximum number of records to return
     * @return CompletableFuture containing the list of products
     */
    CompletableFuture<List<Product>> findAll(int offset, int limit);

    /**
     * Retrieves products by category with pagination support.
     *
     * @param category the product category
     * @param offset the number of records to skip
     * @param limit the maximum number of records to return
     * @return CompletableFuture containing the list of products in the category
     */
    CompletableFuture<List<Product>> findByCategory(String category, int offset, int limit);

    /**
     * Updates an existing product.
     *
     * @param product the product with updated information
     * @return CompletableFuture containing the updated product
     */
    CompletableFuture<Product> update(Product product);

    /**
     * Deletes a product by its ID.
     *
     * @param id the product ID
     * @return CompletableFuture containing true if the product was deleted, false if not found
     */
    CompletableFuture<Boolean> deleteById(String id);

    /**
     * Counts the total number of products.
     *
     * @return CompletableFuture containing the total count
     */
    CompletableFuture<Long> count();

    /**
     * Counts the number of products in a specific category.
     *
     * @param category the product category
     * @return CompletableFuture containing the count for the category
     */
    CompletableFuture<Long> countByCategory(String category);

    /**
     * Checks if a product with the given SKU exists.
     *
     * @param sku the product SKU
     * @return CompletableFuture containing true if exists, false otherwise
     */
    CompletableFuture<Boolean> existsBySku(String sku);

    /**
     * Generic method to check if a product exists by any field.
     *
     * @param field the product field to search by (type-safe enum)
     * @param value the value to search for
     * @return CompletableFuture containing true if exists, false otherwise
     */
    CompletableFuture<Boolean> existsBy(ProductField field, String value);
}
