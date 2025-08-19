/**
 * Product.java
 *
 * <p>Design Doc: ./docs/API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Represents the Product entity in the database.
 *
 * <p>Logic Overview: - This class is a JPA entity mapped to the "products" table. - It includes
 * fields for product details like SKU, name, price, etc. - It uses Lombok for boilerplate code
 * reduction and JPA annotations for ORM. - Timestamps for creation and update are automatically
 * managed.
 *
 * <p>Last Updated: 2025-07-30 by Cline (Model: claude-3-opus, Task: Define Product JPA Entity)
 */
package com.thedavestack.productcatalog.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.thedavestack.productcatalog.converter.CategoryConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ? AND version = ?")
@Where(clause = "deleted = false")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Convert(converter = CategoryConverter.class)
    private Category category;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "min_stock_level")
    private Integer minStockLevel = 0;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Version private Long version;
}
