package com.thedavestack.productcatalog.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.thedavestack.productcatalog.dto.BatchCreateProductRequest;
import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductListResponse;
import com.thedavestack.productcatalog.dto.ProductResponse;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@HelidonTest
@Testcontainers
@DisplayName("ProductResource Integration Tests")
class ProductResourceIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.5")
                    .withDatabaseName("product_catalog_test")
                    .withUsername("test_user")
                    .withPassword("test_pass");

    @Inject private WebTarget webTarget;

    @BeforeAll
    static void beforeAll() {
        System.setProperty(
                "javax.sql.DataSource.product-catalog.dataSource.url", postgres.getJdbcUrl());
        System.setProperty(
                "javax.sql.DataSource.product-catalog.dataSource.username", postgres.getUsername());
        System.setProperty(
                "javax.sql.DataSource.product-catalog.dataSource.password", postgres.getPassword());

        // Initialize database schema
        try (Connection connection = postgres.createConnection("");
                Statement statement = connection.createStatement()) {

            String schema = Files.readString(Paths.get("src/test/resources/schema-test.sql"));
            statement.execute(schema);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    @AfterEach
    void cleanup() {
        try {
            webTarget
                    .path("/api/v1/products")
                    .request()
                    .get(ProductListResponse.class)
                    .products()
                    .forEach(
                            product -> {
                                webTarget
                                        .path("/api/v1/products")
                                        .path(product.id())
                                        .request()
                                        .delete();
                            });
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    @DisplayName("should create product successfully")
    void shouldCreateProductSuccessfully() {
        CreateProductRequest request =
                new CreateProductRequest(
                        "ELC-123456",
                        "Test Product",
                        "Test Description",
                        new BigDecimal("99.99"),
                        "Electronics");

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(request));

        assertThat(response.getStatus(), equalTo(201));

        ProductResponse productResponse = response.readEntity(ProductResponse.class);
        assertThat(productResponse.sku(), equalTo("ELC-123456"));
        assertThat(productResponse.name(), equalTo("Test Product"));
        assertThat(productResponse.description(), equalTo("Test Description"));
        assertThat(productResponse.price(), equalTo(new BigDecimal("99.99")));
        assertThat(productResponse.category(), equalTo("Electronics"));
        assertThat(productResponse.id(), not(nullValue()));
        assertThat(productResponse.createdAt(), not(nullValue()));
        assertThat(productResponse.updatedAt(), not(nullValue()));
    }

    @Test
    @DisplayName("should generate SKU when not provided")
    void shouldGenerateSkuWhenNotProvided() {
        CreateProductRequest request =
                new CreateProductRequest(
                        null,
                        "Test Product Without SKU",
                        "Test Description",
                        new BigDecimal("49.99"),
                        "Electronics");

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(request));

        assertThat(response.getStatus(), equalTo(201));

        ProductResponse productResponse = response.readEntity(ProductResponse.class);
        assertThat(productResponse.sku(), not(nullValue()));
        assertThat(productResponse.sku().startsWith("ELC-"), is(true));
        assertThat(productResponse.name(), equalTo("Test Product Without SKU"));
    }

    @Test
    @DisplayName("should return 400 when name is missing")
    void shouldReturn400WhenNameIsMissing() {
        CreateProductRequest request =
                new CreateProductRequest(
                        "ELC-123456", null, "Test Description", new BigDecimal("99.99"), "Electronics");

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(request));

        assertThat(response.getStatus(), equalTo(400));
        String errorMessage = response.readEntity(String.class);
        assertThat(errorMessage, containsString("name"));
    }

    @Test
    @DisplayName("should return 409 when SKU already exists")
    void shouldReturn409WhenSkuAlreadyExists() {
        CreateProductRequest request1 =
                new CreateProductRequest(
                        "ELC-123456",
                        "First Product",
                        "First Description",
                        new BigDecimal("99.99"),
                        "Electronics");

        webTarget
                .path("/api/v1/products")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request1));

        CreateProductRequest request2 =
                new CreateProductRequest(
                        "ELC-123456",
                        "Second Product",
                        "Second Description",
                        new BigDecimal("149.99"),
                        "Electronics");

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(request2));

        assertThat(response.getStatus(), equalTo(409));
        String errorMessage = response.readEntity(String.class);
        assertThat(errorMessage, containsString("SKU already exists"));
    }

    @Test
    @DisplayName("should create multiple products successfully")
    void shouldCreateMultipleProductsSuccessfully() {
        BatchCreateProductRequest request =
                new BatchCreateProductRequest(
                        List.of(
                                new CreateProductRequest(
                                        "ELC-111111",
                                        "Electronics Product",
                                        "Electronics Description",
                                        new BigDecimal("199.99"),
                                        "Electronics"),
                                new CreateProductRequest(
                                        "CLO-222222",
                                        "Clothing Product",
                                        "Clothing Description",
                                        new BigDecimal("59.99"),
                                        "Clothing")));

        Response response =
                webTarget
                        .path("/api/v1/products/batch-create")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(request));

        assertThat(response.getStatus(), equalTo(201));

        @SuppressWarnings("unchecked")
        List<ProductResponse> products = response.readEntity(List.class);
        assertThat(products.size(), equalTo(2));
    }

    @Test
    @DisplayName("should return product when found")
    void shouldReturnProductWhenFound() {
        CreateProductRequest createRequest =
                new CreateProductRequest(
                        "ELC-333333",
                        "Get Test Product",
                        "Get Test Description",
                        new BigDecimal("79.99"),
                        "Electronics");

        Response createResponse =
                webTarget
                        .path("/api/v1/products")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(createRequest));

        ProductResponse createdProduct = createResponse.readEntity(ProductResponse.class);

        Response getResponse =
                webTarget
                        .path("/api/v1/products")
                        .path(createdProduct.id())
                        .request(MediaType.APPLICATION_JSON)
                        .get();

        assertThat(getResponse.getStatus(), equalTo(200));

        ProductResponse product = getResponse.readEntity(ProductResponse.class);
        assertThat(product.id(), equalTo(createdProduct.id()));
        assertThat(product.sku(), equalTo("ELC-333333"));
        assertThat(product.name(), equalTo("Get Test Product"));
    }

    @Test
    @DisplayName("should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() {
        Response response =
                webTarget
                        .path("/api/v1/products")
                        .path("non-existent-id")
                        .request(MediaType.APPLICATION_JSON)
                        .get();

        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    @DisplayName("should return all products with pagination")
    void shouldReturnAllProductsWithPagination() {
        createMultipleTestProducts();

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .request(MediaType.APPLICATION_JSON)
                        .get();

        assertThat(response.getStatus(), equalTo(200));

        ProductListResponse productList = response.readEntity(ProductListResponse.class);
        assertThat(productList.products().size(), greaterThan(0));
        assertThat(productList.page(), equalTo(0));
        assertThat(productList.size(), equalTo(10));
        assertThat(productList.totalElements(), greaterThan(0L));
    }

    @Test
    @DisplayName("should filter products by category")
    void shouldFilterProductsByCategory() {
        createMultipleTestProducts();

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .queryParam("category", "Electronics")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .request(MediaType.APPLICATION_JSON)
                        .get();

        assertThat(response.getStatus(), equalTo(200));

        ProductListResponse productList = response.readEntity(ProductListResponse.class);
        productList
                .products()
                .forEach(product -> assertThat(product.category(), equalTo("Electronics")));
    }

    @Test
    @DisplayName("should update product successfully")
    void shouldUpdateProductSuccessfully() {
        ProductResponse createdProduct = createTestProduct();

        CreateProductRequest updateRequest =
                new CreateProductRequest(
                        "ELC-999999",
                        "Updated Product Name",
                        "Updated Description",
                        new BigDecimal("149.99"),
                        "Electronics");

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .path(createdProduct.id())
                        .request(MediaType.APPLICATION_JSON)
                        .put(Entity.json(updateRequest));

        assertThat(response.getStatus(), equalTo(200));

        ProductResponse updatedProduct = response.readEntity(ProductResponse.class);
        assertThat(updatedProduct.id(), equalTo(createdProduct.id()));
        assertThat(updatedProduct.sku(), equalTo("ELC-999999"));
        assertThat(updatedProduct.name(), equalTo("Updated Product Name"));
        assertThat(updatedProduct.description(), equalTo("Updated Description"));
        assertThat(updatedProduct.price(), equalTo(new BigDecimal("149.99")));
    }

    @Test
    @DisplayName("should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        ProductResponse createdProduct = createTestProduct();

        Response response =
                webTarget.path("/api/v1/products").path(createdProduct.id()).request().delete();

        assertThat(response.getStatus(), equalTo(204));

        Response getResponse =
                webTarget
                        .path("/api/v1/products")
                        .path(createdProduct.id())
                        .request(MediaType.APPLICATION_JSON)
                        .get();

        assertThat(getResponse.getStatus(), equalTo(404));
    }

    @Test
    @DisplayName("should export products as JSON")
    void shouldExportProductsAsJson() {
        createTestProduct();

        Response response =
                webTarget
                        .path("/api/v1/products/export")
                        .queryParam("format", "json")
                        .request(MediaType.APPLICATION_JSON)
                        .get();

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getHeaderString("Content-Disposition"), containsString("attachment"));
        assertThat(
                response.getHeaderString("Content-Disposition"), containsString("products.json"));

        String jsonResponse = response.readEntity(String.class);
        assertThat(jsonResponse, not(nullValue()));
        assertThat(jsonResponse.length(), greaterThan(0));
    }

    private ProductResponse createTestProduct() {
        CreateProductRequest request =
                new CreateProductRequest(
                        "ELC-555555",
                        "Integration Test Product",
                        "Integration Test Description",
                        new BigDecimal("99.99"),
                        "Electronics");

        Response response =
                webTarget
                        .path("/api/v1/products")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(request));

        return response.readEntity(ProductResponse.class);
    }

    private void createMultipleTestProducts() {
        List<CreateProductRequest> requests =
                List.of(
                        new CreateProductRequest(
                                "ELC-777777",
                                "Electronics Product 1",
                                "Electronics Description 1",
                                new BigDecimal("199.99"),
                                "Electronics"),
                        new CreateProductRequest(
                                "CLO-888888",
                                "Clothing Product 1",
                                "Clothing Description 1",
                                new BigDecimal("59.99"),
                                "Clothing"),
                        new CreateProductRequest(
                                "BOO-999999",
                                "Books Product 1",
                                "Books Description 1",
                                new BigDecimal("29.99"),
                                "Books"));

        for (CreateProductRequest request : requests) {
            webTarget
                    .path("/api/v1/products")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(request));
        }
    }
}