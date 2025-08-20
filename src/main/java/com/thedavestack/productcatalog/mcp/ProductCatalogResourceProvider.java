package com.thedavestack.productcatalog.mcp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedavestack.productcatalog.model.Category;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.service.ProductService;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP Resource Provider for Product Catalog data. Provides dynamic access to product information,
 * inventory data, and catalog statistics for AI interactions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCatalogResourceProvider {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    /**
     * Provides MCP resource specifications for product catalog data.
     *
     * @return List of synchronous resource specifications
     */
    public List<McpServerFeatures.SyncResourceSpecification> getResourceSpecifications() {
        return List.of(
                // All products resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "product://catalog/all",
                                "All Products",
                                "Complete list of all products in the catalog",
                                "application/json",
                                null),
                        this::getAllProductsResource),

                // Products by category resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "product://catalog/category/{category}",
                                "Products by Category",
                                "Products filtered by category (ELECTRONICS, BOOKS, CLOTHING, HOME_GARDEN, SPORTS, TOYS, HEALTH_BEAUTY, AUTOMOTIVE)",
                                "application/json",
                                null),
                        this::getProductsByCategoryResource),

                // Low stock products resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "product://inventory/low-stock",
                                "Low Stock Products",
                                "Products that are below their minimum stock level",
                                "application/json",
                                null),
                        this::getLowStockProductsResource),

                // Catalog summary resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "product://catalog/summary",
                                "Catalog Summary",
                                "High-level statistics and summary of the product catalog",
                                "application/json",
                                null),
                        this::getCatalogSummaryResource),

                // Product by SKU resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "product://catalog/sku/{sku}",
                                "Product by SKU",
                                "Individual product details by SKU identifier",
                                "application/json",
                                null),
                        this::getProductBySkuResource));
    }

    private McpSchema.ReadResourceResult getAllProductsResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Product> products = productService.findAllProducts();
            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "totalCount", products.size(),
                                    "products",
                                            products.stream()
                                                    .map(this::toProductSummary)
                                                    .collect(Collectors.toList())));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get all products resource", e);
            throw new RuntimeException("Failed to retrieve product catalog", e);
        }
    }

    private McpSchema.ReadResourceResult getProductsByCategoryResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            String uriPath = request.uri().toString();
            String categoryName = extractPathParameter(uriPath, "category");

            if (categoryName == null) {
                throw new IllegalArgumentException("Category parameter is required");
            }

            Category category = Category.valueOf(categoryName.toUpperCase());
            Page<Product> products =
                    productService.findWithFilters(null, category, true, PageRequest.of(0, 100));

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "category", category.name(),
                                    "totalCount", products.getTotalElements(),
                                    "products",
                                            products.getContent().stream()
                                                    .map(this::toProductSummary)
                                                    .collect(Collectors.toList())));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get products by category resource", e);
            throw new RuntimeException("Failed to retrieve products by category", e);
        }
    }

    private McpSchema.ReadResourceResult getLowStockProductsResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Page<Product> lowStockProducts =
                    productService.findLowStockProducts(PageRequest.of(0, 100));

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "totalCount", lowStockProducts.getTotalElements(),
                                    "products",
                                            lowStockProducts.getContent().stream()
                                                    .map(
                                                            product -> {
                                                                var summary =
                                                                        toProductSummary(product);
                                                                summary.put(
                                                                        "stockAlert",
                                                                        "Stock ("
                                                                                + product
                                                                                        .getStockQuantity()
                                                                                + ") is below minimum ("
                                                                                + product
                                                                                        .getMinStockLevel()
                                                                                + ")");
                                                                return summary;
                                                            })
                                                    .collect(Collectors.toList())));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get low stock products resource", e);
            throw new RuntimeException("Failed to retrieve low stock products", e);
        }
    }

    private McpSchema.ReadResourceResult getCatalogSummaryResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Product> allProducts = productService.findAllProducts();

            Map<Category, Long> productsByCategory =
                    allProducts.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            Product::getCategory, Collectors.counting()));

            long totalProducts = allProducts.size();
            long activeProducts = allProducts.stream().mapToLong(p -> p.getActive() ? 1 : 0).sum();

            double averagePrice =
                    allProducts.stream()
                            .filter(p -> p.getPrice() != null)
                            .mapToDouble(p -> p.getPrice().doubleValue())
                            .average()
                            .orElse(0.0);

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "totalProducts",
                                    totalProducts,
                                    "activeProducts",
                                    activeProducts,
                                    "inactiveProducts",
                                    totalProducts - activeProducts,
                                    "averagePrice",
                                    String.format("%.2f", averagePrice),
                                    "productsByCategory",
                                    productsByCategory,
                                    "lastUpdated",
                                    java.time.Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get catalog summary resource", e);
            throw new RuntimeException("Failed to retrieve catalog summary", e);
        }
    }

    private McpSchema.ReadResourceResult getProductBySkuResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            String uriPath = request.uri().toString();
            String sku = extractPathParameter(uriPath, "sku");

            if (sku == null) {
                throw new IllegalArgumentException("SKU parameter is required");
            }

            // Find product by SKU (we'd need to add this method to ProductService)
            List<Product> allProducts = productService.findAllProducts();
            Product product =
                    allProducts.stream()
                            .filter(p -> sku.equals(p.getSku()))
                            .findFirst()
                            .orElse(null);

            if (product == null) {
                throw new RuntimeException("Product with SKU '" + sku + "' not found");
            }

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of("product", toDetailedProductInfo(product)));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get product by SKU resource", e);
            throw new RuntimeException("Failed to retrieve product by SKU", e);
        }
    }

    private Map<String, Object> toProductSummary(Product product) {
        return Map.of(
                "id", product.getId(),
                "sku", product.getSku(),
                "name", product.getName(),
                "category", product.getCategory().name(),
                "price", product.getPrice(),
                "stockQuantity", product.getStockQuantity(),
                "active", product.getActive());
    }

    private Map<String, Object> toDetailedProductInfo(Product product) {
        Map<String, Object> result = new HashMap<>();
        result.putAll(
                Map.of(
                        "id", product.getId(),
                        "sku", product.getSku(),
                        "name", product.getName(),
                        "description",
                                product.getDescription() != null ? product.getDescription() : "",
                        "category", product.getCategory().name(),
                        "price", product.getPrice(),
                        "stockQuantity", product.getStockQuantity(),
                        "minStockLevel", product.getMinStockLevel(),
                        "weight", product.getWeight() != null ? product.getWeight() : 0.0,
                        "dimensions",
                                product.getDimensions() != null ? product.getDimensions() : ""));
        result.putAll(
                Map.of(
                        "imageUrl", product.getImageUrl() != null ? product.getImageUrl() : "",
                        "active", product.getActive(),
                        "createdAt", product.getCreatedAt(),
                        "updatedAt", product.getUpdatedAt()));
        return result;
    }

    private String extractPathParameter(String uriPath, String paramName) {
        // Simple path parameter extraction for {paramName}
        String pattern = "\\{" + paramName + "\\}";
        String[] pathParts = uriPath.split("/");
        String[] templateParts = ("product://catalog/category/{category}").split("/");

        for (int i = 0; i < Math.min(pathParts.length, templateParts.length); i++) {
            if (templateParts[i].equals("{" + paramName + "}")) {
                return pathParts[i];
            }
        }
        return null;
    }
}
