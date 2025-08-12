package com.thedavestack.productcatalog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedavestack.productcatalog.BaseIntegrationTest;
import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductPageResponse;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.repository.ProductRepository;
import com.thedavestack.productcatalog.security.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
class ProductControllerEnhancedIT extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext webApplicationContext;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private ProductRepository productRepository;

    @Autowired private JwtUtil jwtUtil;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        adminToken = "Bearer " + jwtUtil.generateToken("admin", "ADMIN");
        userToken = "Bearer " + jwtUtil.generateToken("user", "USER");
        productRepository.deleteAll();
    }

    @Test
    void createProduct_withValidData_shouldReturnCreated() throws Exception {
        CreateProductRequest request =
                new CreateProductRequest(
                        "Test Product",
                        "Test Description",
                        new BigDecimal("99.99"),
                        "TEST-SKU-001",
                        Category.ELECTRONICS,
                        100,
                        10,
                        "https://example.com/image.jpg",
                        new BigDecimal("1.5"),
                        "10x10x5 cm",
                        true);

        MvcResult result =
                mockMvc.perform(
                                post("/api/v1/products")
                                        .header("Authorization", adminToken)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("Test Product"))
                        .andExpect(jsonPath("$.category").value("Electronics"))
                        .andExpect(jsonPath("$.stockQuantity").value(100))
                        .andReturn();

        ProductResponse response =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(), ProductResponse.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.sku()).isEqualTo("TEST-SKU-001");
        assertThat(response.active()).isTrue();
    }

    @Test
    void createProduct_withoutAdminRole_shouldReturnForbidden() throws Exception {
        CreateProductRequest request =
                new CreateProductRequest(
                        "Test Product",
                        "Test Description",
                        new BigDecimal("99.99"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

        mockMvc.perform(
                        post("/api/v1/products")
                                .header("Authorization", userToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllProducts_withPagination_shouldReturnPagedResults() throws Exception {
        // Create test products
        createTestProduct("Product 1", Category.ELECTRONICS);
        createTestProduct("Product 2", Category.CLOTHING);
        createTestProduct("Product 3", Category.BOOKS);

        MvcResult result =
                mockMvc.perform(
                                get("/api/v1/products")
                                        .header("Authorization", userToken)
                                        .param("page", "0")
                                        .param("size", "2")
                                        .param("sortBy", "name")
                                        .param("sortDir", "asc"))
                        .andExpect(status().isOk())
                        .andReturn();

        ProductPageResponse response =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(), ProductPageResponse.class);

        assertThat(response.products()).hasSize(2);
        assertThat(response.totalElements()).isEqualTo(3);
        assertThat(response.totalPages()).isEqualTo(2);
        assertThat(response.pageNumber()).isEqualTo(0);
        assertThat(response.hasNext()).isTrue();
    }

    @Test
    void getAllProducts_withCategoryFilter_shouldReturnFilteredResults() throws Exception {
        createTestProduct("Electronics Product", Category.ELECTRONICS);
        createTestProduct("Clothing Product", Category.CLOTHING);

        MvcResult result =
                mockMvc.perform(
                                get("/api/v1/products")
                                        .header("Authorization", userToken)
                                        .param("category", "ELECTRONICS"))
                        .andExpect(status().isOk())
                        .andReturn();

        ProductPageResponse response =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(), ProductPageResponse.class);

        assertThat(response.products()).hasSize(1);
        assertThat(response.products().get(0).category()).isEqualTo(Category.ELECTRONICS);
    }

    @Test
    void getLowStockProducts_shouldReturnProductsWithLowStock() throws Exception {
        // Create product with low stock
        Product lowStockProduct = new Product();
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setPrice(new BigDecimal("50.00"));
        lowStockProduct.setSku("LOW-STOCK-001");
        lowStockProduct.setStockQuantity(5);
        lowStockProduct.setMinStockLevel(10);
        lowStockProduct.setActive(true);
        productRepository.save(lowStockProduct);

        // Create product with sufficient stock
        Product sufficientStockProduct = new Product();
        sufficientStockProduct.setName("Sufficient Stock Product");
        sufficientStockProduct.setPrice(new BigDecimal("75.00"));
        sufficientStockProduct.setSku("SUFFICIENT-001");
        sufficientStockProduct.setStockQuantity(20);
        sufficientStockProduct.setMinStockLevel(10);
        sufficientStockProduct.setActive(true);
        productRepository.save(sufficientStockProduct);

        MvcResult result =
                mockMvc.perform(
                                get("/api/v1/products/low-stock")
                                        .header("Authorization", userToken))
                        .andExpect(status().isOk())
                        .andReturn();

        ProductPageResponse response =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(), ProductPageResponse.class);

        assertThat(response.products()).hasSize(1);
        assertThat(response.products().get(0).name()).isEqualTo("Low Stock Product");
    }

    @Test
    void updateProduct_withValidData_shouldReturnUpdatedProduct() throws Exception {
        Product product = createTestProduct("Original Product", Category.ELECTRONICS);

        String updateJson =
                """
            {
                "name": "Updated Product",
                "price": 149.99,
                "category": "CLOTHING",
                "stockQuantity": 50
            }
            """;

        mockMvc.perform(
                        put("/api/v1/products/" + product.getId())
                                .header("Authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.category").value("Clothing"))
                .andExpect(jsonPath("$.stockQuantity").value(50));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        Product product = createTestProduct("Product To Delete", Category.ELECTRONICS);

        mockMvc.perform(
                        delete("/api/v1/products/" + product.getId())
                                .header("Authorization", adminToken))
                .andExpect(status().isNoContent());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    void getProduct_withInvalidId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/products/invalid-id").header("Authorization", userToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    void createProduct_withInvalidData_shouldReturnValidationErrors() throws Exception {
        String invalidJson =
                """
            {
                "name": "",
                "price": -10,
                "stockQuantity": -5
            }
            """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .header("Authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.validationErrors").isArray());
    }

    private Product createTestProduct(String name, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription("Test description");
        product.setPrice(new BigDecimal("99.99"));
        product.setSku("TEST-" + System.currentTimeMillis());
        product.setCategory(category);
        product.setStockQuantity(100);
        product.setMinStockLevel(10);
        product.setActive(true);
        return productRepository.save(product);
    }
}
