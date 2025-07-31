/**
 * ProductRepository.java
 *
 * Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * Purpose:
 * - Provides the data access layer for the Product entity.
 *
 * Logic Overview:
 * - Extends Spring Data JPA's JpaRepository to provide standard CRUD operations.
 * - Defines custom query methods for finding products by SKU.
 *
 * Last Updated:
 * 2025-07-31 by Cline (Model: claude-3-opus, Task: task-8)
 */
package com.thedavestack.productcatalog.repository;

import com.thedavestack.productcatalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * Finds a product by its unique SKU.
     *
     * @param sku The SKU of the product to find.
     * @return An Optional containing the found product, or empty if not found.
     */
    Optional<Product> findBySku(String sku);

    /**
     * Checks if a product with the given SKU exists.
     *
     * @param sku The SKU to check for.
     * @return true if a product with the given SKU exists, false otherwise.
     */
    boolean existsBySku(String sku);
}
