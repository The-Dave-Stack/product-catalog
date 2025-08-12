/**
 * ProductRepository.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Provides the data access layer for the Product entity.
 *
 * <p>Logic Overview: - Extends Spring Data JPA's JpaRepository to provide standard CRUD operations.
 * - Defines custom query methods for finding products by SKU.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-8)
 */
package com.thedavestack.productcatalog.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.model.Product;

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
    
    /**
     * Finds products by category with pagination.
     *
     * @param category The category to filter by.
     * @param pageable Pagination information.
     * @return A page of products.
     */
    Page<Product> findByCategory(Category category, Pageable pageable);
    
    /**
     * Finds products by active status with pagination.
     *
     * @param active The active status to filter by.
     * @param pageable Pagination information.
     * @return A page of products.
     */
    Page<Product> findByActive(Boolean active, Pageable pageable);
    
    /**
     * Finds products with name containing the search term (case-insensitive).
     *
     * @param name The name to search for.
     * @param pageable Pagination information.
     * @return A page of products.
     */
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Finds products with advanced filtering.
     *
     * @param name Optional name filter.
     * @param category Optional category filter.
     * @param active Optional active status filter.
     * @param pageable Pagination information.
     * @return A page of products.
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:active IS NULL OR p.active = :active)")
    Page<Product> findWithFilters(@Param("name") String name,
                                 @Param("category") Category category,
                                 @Param("active") Boolean active,
                                 Pageable pageable);
    
    /**
     * Finds products with low stock (stock quantity <= min stock level).
     *
     * @param pageable Pagination information.
     * @return A page of products.
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.minStockLevel")
    Page<Product> findLowStockProducts(Pageable pageable);
}
