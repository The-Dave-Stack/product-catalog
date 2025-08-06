/**
 * ProductRepositoryImpl.java
 *
 * <p>Design Doc: ./docs/api-design-product-catalog-helidon.md
 *
 * <p>Purpose: - Implements ProductRepository interface using Helidon DB Client - Provides concrete
 * database operations for product management - Handles SQL queries, result mapping, and error
 * handling
 *
 * <p>Logic Overview: 1. Uses Helidon DbClient for database operations 2. Maps database rows to
 * Product records 3. Implements batch operations using multiple executions 4. Provides proper error
 * handling and logging 5. Generates UUIDs for new products and manages timestamps
 *
 * <p>Last Updated: 2025-08-06 by Cline (Model: claude-3-opus, Task: Implementing ProductRepository)
 */
package com.thedavestack.productcatalog.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.thedavestack.productcatalog.model.Product;

import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbRow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

    private static final Logger LOGGER = Logger.getLogger(ProductRepositoryImpl.class.getName());

    private final DbClient dbClient;

    @Inject
    public ProductRepositoryImpl(DbClient dbClient) {
        this.dbClient = dbClient;
    }

    @Override
    public CompletableFuture<Product> create(Product product) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String id = UUID.randomUUID().toString();
                        Instant now = Instant.now();

                        Product productToCreate =
                                new Product(
                                        id,
                                        product.sku(),
                                        product.name(),
                                        product.description(),
                                        product.price(),
                                        product.category(),
                                        now,
                                        now);

                        String sql =
                                """
                        INSERT INTO products (id, sku, name, description, price, category, created_at, updated_at)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """;

                        long result =
                                dbClient.execute()
                                        .createInsert(sql)
                                        .addParam(productToCreate.id())
                                        .addParam(productToCreate.sku())
                                        .addParam(productToCreate.name())
                                        .addParam(productToCreate.description())
                                        .addParam(productToCreate.price())
                                        .addParam(productToCreate.category())
                                        .addParam(productToCreate.createdAt())
                                        .addParam(productToCreate.updatedAt())
                                        .execute();

                        if (result > 0) {
                            LOGGER.info("Created product with ID: " + productToCreate.id());
                            return productToCreate;
                        } else {
                            throw new RuntimeException("Failed to create product");
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to create product", e);
                        throw new RuntimeException("Failed to create product", e);
                    }
                });
    }

    @Override
    public CompletableFuture<List<Product>> createBatch(List<Product> products) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        List<Product> createdProducts =
                                products.stream()
                                        .map(
                                                product -> {
                                                    String id = UUID.randomUUID().toString();
                                                    Instant now = Instant.now();

                                                    Product productToCreate =
                                                            new Product(
                                                                    id,
                                                                    product.sku(),
                                                                    product.name(),
                                                                    product.description(),
                                                                    product.price(),
                                                                    product.category(),
                                                                    now,
                                                                    now);

                                                    String sql =
                                                            """
                                    INSERT INTO products (id, sku, name, description, price, category, created_at, updated_at)
                                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                                    """;

                                                    long result =
                                                            dbClient.execute()
                                                                    .createInsert(sql)
                                                                    .addParam(productToCreate.id())
                                                                    .addParam(productToCreate.sku())
                                                                    .addParam(
                                                                            productToCreate.name())
                                                                    .addParam(
                                                                            productToCreate
                                                                                    .description())
                                                                    .addParam(
                                                                            productToCreate.price())
                                                                    .addParam(
                                                                            productToCreate
                                                                                    .category())
                                                                    .addParam(
                                                                            productToCreate
                                                                                    .createdAt())
                                                                    .addParam(
                                                                            productToCreate
                                                                                    .updatedAt())
                                                                    .execute();

                                                    if (result > 0) {
                                                        return productToCreate;
                                                    } else {
                                                        throw new RuntimeException(
                                                                "Failed to create product: "
                                                                        + product.sku());
                                                    }
                                                })
                                        .collect(Collectors.toList());

                        LOGGER.info("Created batch of " + createdProducts.size() + " products");
                        return createdProducts;
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to create batch of products", e);
                        throw new RuntimeException("Failed to create batch of products", e);
                    }
                });
    }

    @Override
    public CompletableFuture<Optional<Product>> findById(String id) {
        return findBy(ProductField.ID, id);
    }

    @Override
    public CompletableFuture<Optional<Product>> findBySku(String sku) {
        return findBy(ProductField.SKU, sku);
    }

    @Override
    public CompletableFuture<Optional<Product>> findBy(ProductField field, String value) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql =
                                "SELECT * FROM products WHERE " + field.getColumnName() + " = ?";

                        Optional<DbRow> result =
                                dbClient.execute().createGet(sql).addParam(value).execute();

                        return result.map(this::mapRowToProduct);
                    } catch (Exception e) {
                        LOGGER.log(
                                Level.SEVERE,
                                "Failed to find product by " + field.name() + ": " + value,
                                e);
                        throw new RuntimeException("Failed to find product", e);
                    }
                });
    }

    @Override
    public CompletableFuture<List<Product>> findAllBy(
            ProductField field, String value, int offset, int limit) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql =
                                "SELECT * FROM products WHERE "
                                        + field.getColumnName()
                                        + " = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";

                        List<DbRow> rows =
                                dbClient.execute()
                                        .createQuery(sql)
                                        .addParam(value)
                                        .addParam(limit)
                                        .addParam(offset)
                                        .execute()
                                        .collect(Collectors.toList());

                        return rows.stream()
                                .map(this::mapRowToProduct)
                                .collect(Collectors.toList());
                    } catch (Exception e) {
                        LOGGER.log(
                                Level.SEVERE,
                                "Failed to find products by " + field.name() + ": " + value,
                                e);
                        throw new RuntimeException("Failed to find products", e);
                    }
                });
    }

    @Override
    public CompletableFuture<List<Product>> findAll(int offset, int limit) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql =
                                "SELECT * FROM products ORDER BY created_at DESC LIMIT ? OFFSET ?";

                        List<DbRow> rows =
                                dbClient.execute()
                                        .createQuery(sql)
                                        .addParam(limit)
                                        .addParam(offset)
                                        .execute()
                                        .collect(Collectors.toList());

                        return rows.stream()
                                .map(this::mapRowToProduct)
                                .collect(Collectors.toList());
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to find all products", e);
                        throw new RuntimeException("Failed to find products", e);
                    }
                });
    }

    @Override
    public CompletableFuture<List<Product>> findByCategory(String category, int offset, int limit) {
        return findAllBy(ProductField.CATEGORY, category, offset, limit);
    }

    @Override
    public CompletableFuture<Product> update(Product product) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Instant now = Instant.now();
                        Product updatedProduct =
                                new Product(
                                        product.id(),
                                        product.sku(),
                                        product.name(),
                                        product.description(),
                                        product.price(),
                                        product.category(),
                                        product.createdAt(),
                                        now);

                        String sql =
                                """
                        UPDATE products
                        SET sku = ?, name = ?, description = ?, price = ?, category = ?, updated_at = ?
                        WHERE id = ?
                        """;

                        long result =
                                dbClient.execute()
                                        .createUpdate(sql)
                                        .addParam(updatedProduct.sku())
                                        .addParam(updatedProduct.name())
                                        .addParam(updatedProduct.description())
                                        .addParam(updatedProduct.price())
                                        .addParam(updatedProduct.category())
                                        .addParam(updatedProduct.updatedAt())
                                        .addParam(updatedProduct.id())
                                        .execute();

                        if (result == 0) {
                            throw new RuntimeException(
                                    "Product not found for update: " + product.id());
                        }
                        LOGGER.info("Updated product with ID: " + updatedProduct.id());
                        return updatedProduct;
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to update product: " + product.id(), e);
                        throw new RuntimeException("Failed to update product", e);
                    }
                });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(String id) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql = "DELETE FROM products WHERE id = ?";

                        long result = dbClient.execute().createDelete(sql).addParam(id).execute();

                        boolean deleted = result > 0;
                        if (deleted) {
                            LOGGER.info("Deleted product with ID: " + id);
                        } else {
                            LOGGER.warning("Product not found for deletion: " + id);
                        }
                        return deleted;
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to delete product: " + id, e);
                        throw new RuntimeException("Failed to delete product", e);
                    }
                });
    }

    @Override
    public CompletableFuture<Long> count() {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql = "SELECT COUNT(*) as total FROM products";

                        Optional<DbRow> result = dbClient.execute().createGet(sql).execute();

                        return result.map(row -> row.column("total").as(Long.class).get())
                                .orElse(0L);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to count products", e);
                        throw new RuntimeException("Failed to count products", e);
                    }
                });
    }

    @Override
    public CompletableFuture<Long> countByCategory(String category) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql = "SELECT COUNT(*) as total FROM products WHERE category = ?";

                        Optional<DbRow> result =
                                dbClient.execute().createGet(sql).addParam(category).execute();

                        return result.map(row -> row.column("total").as(Long.class).get())
                                .orElse(0L);
                    } catch (Exception e) {
                        LOGGER.log(
                                Level.SEVERE,
                                "Failed to count products by category: " + category,
                                e);
                        throw new RuntimeException("Failed to count products by category", e);
                    }
                });
    }

    @Override
    public CompletableFuture<Boolean> existsBySku(String sku) {
        return existsBy(ProductField.SKU, sku);
    }

    @Override
    public CompletableFuture<Boolean> existsBy(ProductField field, String value) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String sql =
                                "SELECT 1 FROM products WHERE "
                                        + field.getColumnName()
                                        + " = ? LIMIT 1";

                        Optional<DbRow> result =
                                dbClient.execute().createGet(sql).addParam(value).execute();

                        return result.isPresent();
                    } catch (Exception e) {
                        LOGGER.log(
                                Level.SEVERE,
                                "Failed to check existence by " + field.name() + ": " + value,
                                e);
                        throw new RuntimeException("Failed to check product existence", e);
                    }
                });
    }

    /**
     * Maps a database row to a Product record.
     *
     * @param row the database row
     * @return the mapped Product
     */
    private Product mapRowToProduct(DbRow row) {
        return new Product(
                row.column("id").as(String.class).get(),
                row.column("sku").as(String.class).get(),
                row.column("name").as(String.class).get(),
                row.column("description").as(String.class).get(),
                row.column("price").as(BigDecimal.class).get(),
                row.column("category").as(String.class).get(),
                row.column("created_at").as(Instant.class).get(),
                row.column("updated_at").as(Instant.class).get());
    }
}
