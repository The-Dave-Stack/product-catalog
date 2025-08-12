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
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String category;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp private Instant updatedAt;
}
