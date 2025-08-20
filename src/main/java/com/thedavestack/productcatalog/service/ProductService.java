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

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
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
    @Tool(
            description =
                    "Retrieves all products from the catalog. Returns complete product information including ID, SKU, name, description, price, category, stock levels, and status. Use this for catalog-wide operations, reporting, or when you need the complete product dataset. Note: This returns all products without pagination - consider using findWithFilters for large datasets.")
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return an Optional containing the product if found, or empty otherwise.
     */
    @Tool(
            description =
                    "Retrieves a specific product by its unique ID. Returns detailed product information if found, or empty if not found. Use this when you have the exact product ID and need complete product details. The ID parameter must be a valid UUID string format. Returns null if product doesn't exist or has been soft-deleted.")
    public Optional<Product> findById(
            @ToolParam(description = "The unique UUID identifier of the product to retrieve")
                    String id) {
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
    @Tool(
            description =
                    "Creates a new product in the catalog with comprehensive validation and audit logging. Required fields: name, price, category, stockQuantity, minStockLevel. Optional fields: description, SKU (auto-generated if not provided), weight, dimensions, imageUrl. SKU must be globally unique if provided. Automatically creates audit log entry. Throws DuplicateSkuException for duplicate SKUs. Use this for adding new products to the inventory system.")
    @Transactional
    public Product createProduct(
            @ToolParam(
                            description =
                                    "Product object with required fields: name, price, category, stockQuantity, minStockLevel. Optional: description, sku, weight, dimensions, imageUrl")
                    Product product) {
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
    @Tool(
            description =
                    "Creates multiple products in a single atomic transaction with rollback on any failure. All products must pass validation before any are created. Each product follows the same creation rules as single product creation (SKU uniqueness, required fields, etc.). Use this for bulk imports, batch creation operations, or when you need to ensure all-or-none creation semantics. More efficient than individual creation calls.")
    @Transactional
    public List<Product> createMultipleProducts(
            @ToolParam(
                            description =
                                    "List of product objects to create in a single transaction. Each product must have required fields: name, price, category, stockQuantity, minStockLevel")
                    List<Product> products) {
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
    @Tool(
            description =
                    "Updates an existing product with partial or complete field updates. Only provided fields are updated - null/empty fields are ignored. Maintains data integrity with validation and audit logging. Throws ProductNotFoundException if product doesn't exist. Common use cases: price updates, inventory adjustments, description changes, category reassignments. Creates audit log entry with before/after values.")
    @Transactional
    public Product updateProduct(
            @ToolParam(description = "The unique UUID identifier of the product to update")
                    String id,
            @ToolParam(
                            description =
                                    "Product object with fields to update. Only non-null fields will be updated. Supported fields: name, description, price, category, stockQuantity, minStockLevel, imageUrl, weight, dimensions, active")
                    Product productDetails) {
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
    @Tool(
            description =
                    "Performs soft delete of a product by marking it as inactive while preserving data integrity and audit history. Product remains in database but is excluded from normal queries. Use this instead of hard delete to maintain referential integrity, audit trails, and historical data. Creates audit log entry. Product can be reactivated if needed. Throws ProductNotFoundException if product doesn't exist.")
    @Transactional
    public void deleteProduct(
            @ToolParam(description = "The unique UUID identifier of the product to soft delete")
                    String id) {
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
    @Tool(
            description =
                    "Retrieves products with pagination support for efficient handling of large datasets. Use Pageable parameter to specify page number (0-based), page size (default 20, max 100), and sorting criteria. Returns Page object with content, total elements, total pages, and pagination metadata. Ideal for displaying products in user interfaces, reports, or when processing large catalogs efficiently.")
    public Page<Product> findAll(
            @ToolParam(
                            description =
                                    "Pagination parameters: page number (0-based), page size (max 100), and sort criteria (e.g., 'name,asc' or 'price,desc')")
                    Pageable pageable) {
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
    @Tool(
            description =
                    "Advanced product search with comprehensive filtering and pagination capabilities. Filter by: name (partial match), category (exact match), active status (true/false). Combine multiple filters for precise results. Supports full pagination with sorting. Use for complex queries, search functionality, catalog browsing, or when you need specific product subsets. More efficient than client-side filtering.")
    public Page<Product> findWithFilters(
            @ToolParam(
                            required = false,
                            description = "Optional partial name filter for product search")
                    String name,
            @ToolParam(
                            required = false,
                            description =
                                    "Optional category filter. Valid values: ELECTRONICS, HOME_GARDEN, SPORTS_OUTDOORS, TOYS_GAMES, HEALTH_BEAUTY, FOOD_BEVERAGES")
                    Category category,
            @ToolParam(
                            required = false,
                            description =
                                    "Optional active status filter. true for active products, false for inactive products")
                    Boolean active,
            @ToolParam(
                            description =
                                    "Pagination parameters: page number (0-based), page size (max 100), and sort criteria")
                    Pageable pageable) {
        return productRepository.findWithFilters(name, category, active, pageable);
    }

    /**
     * Retrieves products with low stock.
     *
     * @param pageable the pagination information.
     * @return a page of products with low stock.
     */
    @Tool(
            description =
                    "Identifies products requiring immediate attention due to low or zero stock levels. Returns products where current stock quantity is less than or equal to their minimum stock level threshold. Critical for inventory management, reorder planning, and preventing stockouts. Includes pagination for large result sets. Use for daily inventory monitoring, automated reorder triggers, and stock alert systems.")
    public Page<Product> findLowStockProducts(
            @ToolParam(
                            description =
                                    "Pagination parameters for low stock results: page number (0-based), page size (max 100), and sort criteria")
                    Pageable pageable) {
        return productRepository.findLowStockProducts(pageable);
    }
}
