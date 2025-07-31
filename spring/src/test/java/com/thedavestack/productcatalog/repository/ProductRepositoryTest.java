/**
 * ProductRepositoryTest.java
 *
 * Purpose:
 * - Contains integration tests for the ProductRepository interface.
 *
 * Logic Overview:
 * - Uses @DataJpaTest to configure an in-memory database and test the repository layer in isolation.
 * - Leverages Testcontainers to run tests against a real PostgreSQL database, ensuring consistency with production.
 * - Tests the custom query methods findBySku and existsBySku.
 *
 * Last Updated:
 * 2025-07-31 by Cline (Model: claude-3-opus, Task: task-8)
 */
package com.thedavestack.productcatalog.repository;

import com.thedavestack.productcatalog.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.5-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findBySku_shouldReturnProduct_whenProductExists() {
        // given
        Product product = new Product();
        product.setSku("test-sku");
        product.setName("Test Product");
        product.setDescription("A product for testing");
        product.setPrice(new BigDecimal("99.99"));
        productRepository.save(product);

        // when
        Optional<Product> foundProduct = productRepository.findBySku("test-sku");

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getSku()).isEqualTo("test-sku");
    }

    @Test
    void findBySku_shouldReturnEmpty_whenProductDoesNotExist() {
        // when
        Optional<Product> foundProduct = productRepository.findBySku("non-existent-sku");

        // then
        assertThat(foundProduct).isNotPresent();
    }

    @Test
    void existsBySku_shouldReturnTrue_whenProductExists() {
        // given
        Product product = new Product();
        product.setSku("test-sku-2");
        product.setName("Another Test Product");
        product.setDescription("Another product for testing");
        product.setPrice(new BigDecimal("199.99"));
        productRepository.save(product);

        // when
        boolean exists = productRepository.existsBySku("test-sku-2");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsBySku_shouldReturnFalse_whenProductDoesNotExist() {
        // when
        boolean exists = productRepository.existsBySku("non-existent-sku-2");

        // then
        assertThat(exists).isFalse();
    }
}
