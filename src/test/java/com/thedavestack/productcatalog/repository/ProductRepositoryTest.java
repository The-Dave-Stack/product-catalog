/**
 * ProductRepositoryTest.java
 *
 * <p>Purpose: - Contains integration tests for the ProductRepository interface.
 *
 * <p>Logic Overview: - Uses @DataJpaTest to configure an in-memory database and test the repository
 * layer in isolation. - Leverages Testcontainers to run tests against a real PostgreSQL database,
 * ensuring consistency with production. - Tests the custom query methods findBySku and existsBySku.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: task-8)
 */
package com.thedavestack.productcatalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.thedavestack.productcatalog.BaseIntegrationTest;
import com.thedavestack.productcatalog.model.Product;

@SpringBootTest
@ActiveProfiles("test")
class ProductRepositoryTest extends BaseIntegrationTest {

    @Autowired private ProductRepository productRepository;

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
