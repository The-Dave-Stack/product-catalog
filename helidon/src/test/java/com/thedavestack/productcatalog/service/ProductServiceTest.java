package com.thedavestack.productcatalog.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock private ProductRepository productRepository;

    @InjectMocks private ProductService productService;

    private Product testProduct;
    private Product testProductWithId;

    @BeforeEach
    void setUp() {
        testProduct =
                new Product(
                        null,
                        "ELC-123456",
                        "Test Product",
                        "Test Description",
                        new BigDecimal("99.99"),
                        "Electronics",
                        null,
                        null);

        testProductWithId =
                new Product(
                        "test-id-123",
                        "ELC-123456",
                        "Test Product",
                        "Test Description",
                        new BigDecimal("99.99"),
                        "Electronics",
                        Instant.now(),
                        Instant.now());
    }

    @Nested
    @DisplayName("createProduct")
    class CreateProduct {

        @Test
        @DisplayName("should create product successfully with provided SKU")
        void shouldCreateProductWithProvidedSku() throws Exception {
            when(productRepository.existsBySku("ELC-123456"))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.create(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(testProductWithId));

            Product result = productService.createProduct(testProduct).get();

            assertThat(result.sku(), equalTo("ELC-123456"));
            assertThat(result.name(), equalTo("Test Product"));
            verify(productRepository).existsBySku("ELC-123456");
            verify(productRepository).create(any(Product.class));
        }

        @Test
        @DisplayName("should generate SKU when not provided")
        void shouldGenerateSkuWhenNotProvided() throws Exception {
            Product productWithoutSku =
                    new Product(
                            null,
                            null,
                            "Test Product",
                            "Test Description",
                            new BigDecimal("99.99"),
                            "Electronics",
                            null,
                            null);

            when(productRepository.existsBySku(anyString()))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.create(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(testProductWithId));

            Product result = productService.createProduct(productWithoutSku).get();

            assertTrue(Pattern.matches("ELC-\\d{6}", result.sku()));
            verify(productRepository).existsBySku(anyString());
            verify(productRepository).create(any(Product.class));
        }

        @Test
        @DisplayName("should throw exception when SKU already exists")
        void shouldThrowExceptionWhenSkuExists() {
            when(productRepository.existsBySku("ELC-123456"))
                    .thenReturn(CompletableFuture.completedFuture(true));

            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.createProduct(testProduct).get());

            assertThat(exception.getCause(), is(instanceOf(DuplicateSkuException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    containsString("SKU already exists: ELC-123456"));
        }

        @Test
        @DisplayName("should throw exception when product is null")
        void shouldThrowExceptionWhenProductIsNull() {
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.createProduct(null));

            assertThat(exception.getMessage(), equalTo("Product cannot be null"));
        }

        @Test
        @DisplayName("should throw exception when product name is null or empty")
        void shouldThrowExceptionWhenProductNameIsNullOrEmpty() {
            Product productWithoutName =
                    new Product(
                            null,
                            "ELC-123456",
                            null,
                            "Test Description",
                            new BigDecimal("99.99"),
                            "Electronics",
                            null,
                            null);

            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.createProduct(productWithoutName));

            assertThat(exception.getMessage(), equalTo("Product name cannot be null or empty"));
        }

        @Test
        @DisplayName("should throw exception when price is null or negative")
        void shouldThrowExceptionWhenPriceIsNegative() {
            Product productWithNegativePrice =
                    new Product(
                            null,
                            "ELC-123456",
                            "Test Product",
                            "Test Description",
                            new BigDecimal("-10.00"),
                            "Electronics",
                            null,
                            null);

            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.createProduct(productWithNegativePrice));

            assertThat(exception.getMessage(), equalTo("Product price cannot be null or negative"));
        }

        @Test
        @DisplayName("should throw exception when SKU format is invalid")
        void shouldThrowExceptionWhenSkuFormatIsInvalid() {
            Product productWithInvalidSku =
                    new Product(
                            null,
                            "INVALID-SKU",
                            "Test Product",
                            "Test Description",
                            new BigDecimal("99.99"),
                            "Electronics",
                            null,
                            null);

            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.createProduct(productWithInvalidSku));

            assertThat(
                    exception.getMessage(), equalTo("Invalid SKU format. Expected: [NNN]-######"));
        }
    }

    @Nested
    @DisplayName("createProductsBatch")
    class CreateProductsBatch {

        @Test
        @DisplayName("should create multiple products successfully")
        void shouldCreateMultipleProductsSuccessfully() throws Exception {
            Product product1 =
                    new Product(
                            null,
                            "ELC-123456",
                            "Product 1",
                            "Description 1",
                            new BigDecimal("99.99"),
                            "Electronics",
                            null,
                            null);
            Product product2 =
                    new Product(
                            null,
                            "CLO-654321",
                            "Product 2",
                            "Description 2",
                            new BigDecimal("49.99"),
                            "Clothing",
                            null,
                            null);
            List<Product> products = List.of(product1, product2);

            when(productRepository.existsBySku("ELC-123456"))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.existsBySku("CLO-654321"))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.createBatch(anyList()))
                    .thenReturn(CompletableFuture.completedFuture(List.of(testProductWithId)));

            List<Product> result = productService.createProductsBatch(products).get();

            assertThat(result.size(), equalTo(1));
            verify(productRepository).createBatch(anyList());
        }

        @Test
        @DisplayName("should throw exception when products list is null")
        void shouldThrowExceptionWhenProductsListIsNull() {
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.createProductsBatch(null));

            assertThat(exception.getMessage(), equalTo("Products list cannot be null or empty"));
        }

        @Test
        @DisplayName("should throw exception when products list is empty")
        void shouldThrowExceptionWhenProductsListIsEmpty() {
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.createProductsBatch(List.of()));

            assertThat(exception.getMessage(), equalTo("Products list cannot be null or empty"));
        }

        @Test
        @DisplayName("should throw exception when duplicate SKU exists in batch")
        void shouldThrowExceptionWhenDuplicateSkuExistsInBatch() {
            List<Product> products = List.of(testProduct);

            when(productRepository.existsBySku("ELC-123456"))
                    .thenReturn(CompletableFuture.completedFuture(true));

            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.createProductsBatch(products).get());

            assertThat(exception.getCause(), is(instanceOf(DuplicateSkuException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    containsString("SKU already exists: ELC-123456"));
        }
    }

    @Nested
    @DisplayName("getProductById")
    class GetProductById {

        @Test
        @DisplayName("should return product when found")
        void shouldReturnProductWhenFound() throws Exception {
            when(productRepository.findById("test-id-123"))
                    .thenReturn(CompletableFuture.completedFuture(Optional.of(testProductWithId)));

            Product result = productService.getProductById("test-id-123").get();

            assertThat(result.id(), equalTo("test-id-123"));
            assertThat(result.name(), equalTo("Test Product"));
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            when(productRepository.findById("non-existent-id"))
                    .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductById("non-existent-id").get());

            assertThat(exception.getCause(), is(instanceOf(ProductNotFoundException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    containsString("Product not found with ID: non-existent-id"));
        }

        @Test
        @DisplayName("should throw exception when ID is null")
        void shouldThrowExceptionWhenIdIsNull() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductById(null).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    equalTo("Product ID cannot be null or empty"));
        }

        @Test
        @DisplayName("should throw exception when ID is empty")
        void shouldThrowExceptionWhenIdIsEmpty() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductById("").get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    equalTo("Product ID cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("getProductBySku")
    class GetProductBySku {

        @Test
        @DisplayName("should return product when found by SKU")
        void shouldReturnProductWhenFoundBySku() throws Exception {
            when(productRepository.findBySku("ELC-123456"))
                    .thenReturn(CompletableFuture.completedFuture(Optional.of(testProductWithId)));

            Product result = productService.getProductBySku("ELC-123456").get();

            assertThat(result.sku(), equalTo("ELC-123456"));
            assertThat(result.name(), equalTo("Test Product"));
        }

        @Test
        @DisplayName("should throw exception when product not found by SKU")
        void shouldThrowExceptionWhenProductNotFoundBySku() {
            when(productRepository.findBySku("NON-123456"))
                    .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductBySku("NON-123456").get());

            assertThat(exception.getCause(), is(instanceOf(ProductNotFoundException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    containsString("Product not found with SKU: NON-123456"));
        }

        @Test
        @DisplayName("should throw exception when SKU is null")
        void shouldThrowExceptionWhenSkuIsNull() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductBySku(null).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    equalTo("Product SKU cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("getAllProducts")
    class GetAllProducts {

        @Test
        @DisplayName("should return all products with pagination")
        void shouldReturnAllProductsWithPagination() throws Exception {
            List<Product> products = List.of(testProductWithId);
            when(productRepository.findAll(0, 10))
                    .thenReturn(CompletableFuture.completedFuture(products));

            List<Product> result = productService.getAllProducts(0, 10).get();

            assertThat(result.size(), equalTo(1));
            assertThat(result.get(0).name(), equalTo("Test Product"));
        }

        @Test
        @DisplayName("should throw exception when offset is negative")
        void shouldThrowExceptionWhenOffsetIsNegative() {
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.getAllProducts(-1, 10));

            assertThat(exception.getMessage(), equalTo("Offset cannot be negative"));
        }

        @Test
        @DisplayName("should throw exception when limit is zero or negative")
        void shouldThrowExceptionWhenLimitIsZeroOrNegative() {
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.getAllProducts(0, 0));

            assertThat(exception.getMessage(), equalTo("Limit must be between 1 and 1000"));
        }

        @Test
        @DisplayName("should throw exception when limit exceeds maximum")
        void shouldThrowExceptionWhenLimitExceedsMaximum() {
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> productService.getAllProducts(0, 1001));

            assertThat(exception.getMessage(), equalTo("Limit must be between 1 and 1000"));
        }
    }

    @Nested
    @DisplayName("getProductsByCategory")
    class GetProductsByCategory {

        @Test
        @DisplayName("should return products by category")
        void shouldReturnProductsByCategory() throws Exception {
            List<Product> products = List.of(testProductWithId);
            when(productRepository.findByCategory("Electronics", 0, 10))
                    .thenReturn(CompletableFuture.completedFuture(products));

            List<Product> result = productService.getProductsByCategory("Electronics", 0, 10).get();

            assertThat(result.size(), equalTo(1));
            assertThat(result.get(0).category(), equalTo("Electronics"));
        }

        @Test
        @DisplayName("should throw exception when category is null")
        void shouldThrowExceptionWhenCategoryIsNull() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductsByCategory(null, 0, 10).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(), equalTo("Category cannot be null or empty"));
        }

        @Test
        @DisplayName("should throw exception when category is empty")
        void shouldThrowExceptionWhenCategoryIsEmpty() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.getProductsByCategory("", 0, 10).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(), equalTo("Category cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("updateProduct")
    class UpdateProduct {

        @Test
        @DisplayName("should update product successfully")
        void shouldUpdateProductSuccessfully() throws Exception {
            Product updatedProduct =
                    new Product(
                            "test-id-123",
                            "ELC-123456",
                            "Updated Product",
                            "Updated Description",
                            new BigDecimal("149.99"),
                            "Electronics",
                            null,
                            null);

            when(productRepository.findById("test-id-123"))
                    .thenReturn(CompletableFuture.completedFuture(Optional.of(testProductWithId)));
            when(productRepository.update(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(updatedProduct));

            Product result = productService.updateProduct(updatedProduct).get();

            assertThat(result.name(), equalTo("Updated Product"));
            assertThat(result.price(), equalTo(new BigDecimal("149.99")));
        }

        @Test
        @DisplayName("should throw exception when product not found for update")
        void shouldThrowExceptionWhenProductNotFoundForUpdate() {
            Product updatedProduct =
                    new Product(
                            "non-existent-id",
                            "ELC-123456",
                            "Updated Product",
                            "Updated Description",
                            new BigDecimal("149.99"),
                            "Electronics",
                            null,
                            null);

            when(productRepository.findById("non-existent-id"))
                    .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.updateProduct(updatedProduct).get());

            assertThat(exception.getCause(), is(instanceOf(ProductNotFoundException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    containsString("Product not found with ID: non-existent-id"));
        }

        @Test
        @DisplayName("should throw exception when product or ID is null")
        void shouldThrowExceptionWhenProductOrIdIsNull() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.updateProduct(null).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    equalTo("Product and ID cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("deleteProduct")
    class DeleteProduct {

        @Test
        @DisplayName("should delete product successfully")
        void shouldDeleteProductSuccessfully() throws Exception {
            when(productRepository.deleteById("test-id-123"))
                    .thenReturn(CompletableFuture.completedFuture(true));

            Boolean result = productService.deleteProduct("test-id-123").get();

            assertThat(result, is(true));
        }

        @Test
        @DisplayName("should throw exception when product not found for deletion")
        void shouldThrowExceptionWhenProductNotFoundForDeletion() {
            when(productRepository.deleteById("non-existent-id"))
                    .thenReturn(CompletableFuture.completedFuture(false));

            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.deleteProduct("non-existent-id").get());

            assertThat(exception.getCause(), is(instanceOf(ProductNotFoundException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    containsString("Product not found with ID: non-existent-id"));
        }

        @Test
        @DisplayName("should throw exception when ID is null")
        void shouldThrowExceptionWhenIdIsNullForDeletion() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.deleteProduct(null).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(),
                    equalTo("Product ID cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("countProducts")
    class CountProducts {

        @Test
        @DisplayName("should count all products")
        void shouldCountAllProducts() throws Exception {
            when(productRepository.count()).thenReturn(CompletableFuture.completedFuture(10L));

            Long result = productService.countProducts().get();

            assertThat(result, equalTo(10L));
        }
    }

    @Nested
    @DisplayName("countProductsByCategory")
    class CountProductsByCategory {

        @Test
        @DisplayName("should count products by category")
        void shouldCountProductsByCategory() throws Exception {
            when(productRepository.countByCategory("Electronics"))
                    .thenReturn(CompletableFuture.completedFuture(5L));

            Long result = productService.countProductsByCategory("Electronics").get();

            assertThat(result, equalTo(5L));
        }

        @Test
        @DisplayName("should throw exception when category is null")
        void shouldThrowExceptionWhenCategoryIsNullForCount() {
            ExecutionException exception =
                    assertThrows(
                            ExecutionException.class,
                            () -> productService.countProductsByCategory(null).get());

            assertThat(exception.getCause(), is(instanceOf(IllegalArgumentException.class)));
            assertThat(
                    exception.getCause().getMessage(), equalTo("Category cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("SKU Generation")
    class SkuGeneration {

        @Test
        @DisplayName("should generate SKU with Electronics prefix for Electronics category")
        void shouldGenerateSkuWithElectronicsPrefix() throws Exception {
            Product productWithoutSku =
                    new Product(
                            null,
                            null,
                            "Test Product",
                            "Test Description",
                            new BigDecimal("99.99"),
                            "Electronics",
                            null,
                            null);

            when(productRepository.existsBySku(anyString()))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.create(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(testProductWithId));

            Product result = productService.createProduct(productWithoutSku).get();

            assertTrue(Pattern.matches("ELC-\\d{6}", result.sku()));
        }

        @Test
        @DisplayName("should generate SKU with Clothing prefix for Clothing category")
        void shouldGenerateSkuWithClothingPrefix() throws Exception {
            Product productWithoutSku =
                    new Product(
                            null,
                            null,
                            "Test Product",
                            "Test Description",
                            new BigDecimal("99.99"),
                            "Clothing",
                            null,
                            null);

            when(productRepository.existsBySku(anyString()))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.create(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(testProductWithId));

            productService.createProduct(productWithoutSku).get();

            verify(productRepository)
                    .existsBySku(
                            org.mockito.ArgumentMatchers.argThat(sku -> sku.startsWith("CLO-")));
        }

        @Test
        @DisplayName("should generate SKU with first 3 chars for unknown category")
        void shouldGenerateSkuWithFirstThreeCharsForUnknownCategory() throws Exception {
            Product productWithoutSku =
                    new Product(
                            null,
                            null,
                            "Test Product",
                            "Test Description",
                            new BigDecimal("99.99"),
                            "Furniture",
                            null,
                            null);

            when(productRepository.existsBySku(anyString()))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.create(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(testProductWithId));

            productService.createProduct(productWithoutSku).get();

            verify(productRepository)
                    .existsBySku(
                            org.mockito.ArgumentMatchers.argThat(sku -> sku.startsWith("FUR-")));
        }

        @Test
        @DisplayName("should generate SKU when category is null")
        void shouldGenerateSkuWhenCategoryIsNull() throws Exception {
            Product productWithoutSkuOrCategory =
                    new Product(
                            null,
                            null,
                            "Test Product",
                            "Test Description",
                            new BigDecimal("99.99"),
                            null,
                            null,
                            null);

            when(productRepository.existsBySku(anyString()))
                    .thenReturn(CompletableFuture.completedFuture(false));
            when(productRepository.create(any(Product.class)))
                    .thenReturn(CompletableFuture.completedFuture(testProductWithId));

            Product result = productService.createProduct(productWithoutSkuOrCategory).get();

            assertThat(result.sku(), not(nullValue()));
            assertTrue(Pattern.matches("[A-Z]{3}-\\d{6}", result.sku()));
        }
    }
}
