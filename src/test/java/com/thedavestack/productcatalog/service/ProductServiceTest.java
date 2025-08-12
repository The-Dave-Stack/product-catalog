/**
 * ProductServiceTest.java
 *
 * <p>Design Doc: ./docs/API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Contains unit tests for the ProductService class. - Ensures that the business logic
 * of the service layer is correct.
 *
 * <p>Logic Overview: - Uses JUnit 5 and Mockito to test the ProductService in isolation. - Mocks
 * the ProductRepository to simulate database interactions. - Covers happy paths, edge cases, and
 * error conditions.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: Initial creation for task-9)
 */
package com.thedavestack.productcatalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thedavestack.productcatalog.exception.ProductNotFoundException;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;

    @Mock private AuditService auditService;

    @InjectMocks private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId("1");
        product1.setName("Test Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(BigDecimal.valueOf(10.0));

        product2 = new Product();
        product2.setId("2");
        product2.setName("Test Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(BigDecimal.valueOf(20.0));
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).contains(product1, product2);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnProduct_whenProductExists() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product1));

        Optional<Product> foundProduct = productService.findById("1");

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get()).isEqualTo(product1);
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        Product productToCreate = new Product();
        productToCreate.setName("New Product");
        productToCreate.setDescription("New Description");
        productToCreate.setPrice(BigDecimal.valueOf(100.0));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(
                        invocation -> {
                            Product p = invocation.getArgument(0);
                            p.setId("new-id");
                            return p;
                        });

        Product createdProduct = productService.createProduct(productToCreate);

        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isEqualTo("new-id");
        assertThat(createdProduct.getSku()).isNotNull();
        verify(productRepository, times(1)).save(productToCreate);
    }

    @Test
    void createMultipleProducts_shouldSaveAndReturnAllProducts() {
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<Product> productsToCreate = Arrays.asList(new Product(), new Product());
        List<Product> createdProducts = productService.createMultipleProducts(productsToCreate);

        assertThat(createdProducts).hasSize(2);
        createdProducts.forEach(p -> assertThat(p.getSku()).isNotNull());
        verify(productRepository, times(2)).save(any(Product.class));
    }

    @Test
    void createMultipleProducts_shouldReturnEmptyList_whenInputIsEmpty() {
        List<Product> createdProducts = productService.createMultipleProducts(List.of());

        assertThat(createdProducts).isNotNull();
        assertThat(createdProducts).isEmpty();
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        Product productDetails = new Product();
        productDetails.setName("Updated Name");
        productDetails.setDescription("Updated Description");
        productDetails.setPrice(BigDecimal.valueOf(15.0));

        Product updatedProduct = productService.updateProduct("1", productDetails);

        assertThat(updatedProduct.getName()).isEqualTo("Updated Name");
        assertThat(updatedProduct.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(15.0));
        verify(productRepository, times(1)).findById("1");
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void deleteProduct_shouldDeleteProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product1));
        doNothing().when(productRepository).delete(product1);

        productService.deleteProduct("1");

        verify(productRepository, times(1)).findById("1");
        verify(productRepository, times(1)).delete(product1);
    }

    @Test
    void findById_shouldReturnEmpty_whenProductDoesNotExist() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productService.findById("1");

        assertThat(foundProduct).isNotPresent();
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void updateProduct_shouldThrowException_whenProductDoesNotExist() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        Product productDetails = new Product();

        assertThrows(
                ProductNotFoundException.class,
                () -> {
                    productService.updateProduct("1", productDetails);
                });

        verify(productRepository, times(1)).findById("1");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductDoesNotExist() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> {
                    productService.deleteProduct("1");
                });

        verify(productRepository, times(1)).findById("1");
        verify(productRepository, never()).delete(any(Product.class));
    }
}
