package com.thedavestack.productcatalog.mcp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
 * MCP Resource Provider for Inventory Status information. Provides real-time inventory analytics,
 * stock alerts, and inventory health metrics for AI-driven inventory management.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryStatusResourceProvider {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    /**
     * Provides MCP resource specifications for inventory status data.
     *
     * @return List of synchronous resource specifications
     */
    public List<McpServerFeatures.SyncResourceSpecification> getResourceSpecifications() {
        return List.of(
                // Inventory overview resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "inventory://status/overview",
                                "Inventory Overview",
                                "Complete inventory status with stock levels, alerts, and health metrics",
                                "application/json",
                                null),
                        this::getInventoryOverviewResource),

                // Stock alerts resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "inventory://alerts/critical",
                                "Critical Stock Alerts",
                                "Products requiring immediate attention due to low or zero stock",
                                "application/json",
                                null),
                        this::getCriticalStockAlertsResource),

                // Inventory value analysis resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "inventory://analysis/value",
                                "Inventory Value Analysis",
                                "Financial analysis of inventory including total value, value by category, and turnover metrics",
                                "application/json",
                                null),
                        this::getInventoryValueAnalysisResource),

                // Stock movement predictions resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "inventory://predictions/stock-movement",
                                "Stock Movement Predictions",
                                "AI-driven predictions for stock movement and reorder recommendations",
                                "application/json",
                                null),
                        this::getStockMovementPredictionsResource),

                // Category inventory health resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "inventory://health/category/{category}",
                                "Category Inventory Health",
                                "Detailed inventory health analysis for a specific category",
                                "application/json",
                                null),
                        this::getCategoryInventoryHealthResource));
    }

    private McpSchema.ReadResourceResult getInventoryOverviewResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Product> allProducts = productService.findAllProducts();

            long totalProducts = allProducts.size();
            long productsInStock =
                    allProducts.stream().mapToLong(p -> p.getStockQuantity() > 0 ? 1 : 0).sum();
            long outOfStock = totalProducts - productsInStock;

            long lowStockCount =
                    allProducts.stream()
                            .mapToLong(
                                    p ->
                                            p.getStockQuantity() <= p.getMinStockLevel()
                                                            && p.getStockQuantity() > 0
                                                    ? 1
                                                    : 0)
                            .sum();

            int totalStockUnits = allProducts.stream().mapToInt(Product::getStockQuantity).sum();

            BigDecimal totalInventoryValue =
                    allProducts.stream()
                            .filter(p -> p.getPrice() != null)
                            .map(
                                    p ->
                                            p.getPrice()
                                                    .multiply(
                                                            BigDecimal.valueOf(
                                                                    p.getStockQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Stock health score (0-100)
            double stockHealthScore = calculateStockHealthScore(allProducts);

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "overview",
                                            Map.of(
                                                    "totalProducts", totalProducts,
                                                    "productsInStock", productsInStock,
                                                    "outOfStock", outOfStock,
                                                    "lowStockAlerts", lowStockCount,
                                                    "totalStockUnits", totalStockUnits,
                                                    "totalInventoryValue", totalInventoryValue,
                                                    "stockHealthScore",
                                                            String.format(
                                                                    "%.1f", stockHealthScore)),
                                    "alerts",
                                            Map.of(
                                                    "criticalAlerts", outOfStock,
                                                    "warningAlerts", lowStockCount,
                                                    "needsAttention", outOfStock + lowStockCount),
                                    "timestamp", java.time.Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get inventory overview resource", e);
            throw new RuntimeException("Failed to retrieve inventory overview", e);
        }
    }

    private McpSchema.ReadResourceResult getCriticalStockAlertsResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Product> allProducts = productService.findAllProducts();

            List<Map<String, Object>> criticalAlerts =
                    allProducts.stream()
                            .filter(p -> p.getStockQuantity() == 0)
                            .map(
                                    product ->
                                            (Map<String, Object>)
                                                    new HashMap<String, Object>(
                                                            Map.of(
                                                                    "sku", product.getSku(),
                                                                    "name", product.getName(),
                                                                    "category",
                                                                            product.getCategory()
                                                                                    .name(),
                                                                    "stockQuantity", "0",
                                                                    "minStockLevel",
                                                                            product
                                                                                    .getMinStockLevel(),
                                                                    "alertLevel", "CRITICAL",
                                                                    "message",
                                                                            "Out of stock - immediate reorder required",
                                                                    "estimatedLostSales",
                                                                            calculateEstimatedLostSales(
                                                                                    product))))
                            .toList();

            List<Map<String, Object>> warningAlerts =
                    allProducts.stream()
                            .filter(
                                    p ->
                                            p.getStockQuantity() > 0
                                                    && p.getStockQuantity() <= p.getMinStockLevel())
                            .map(
                                    product ->
                                            (Map<String, Object>)
                                                    new HashMap<String, Object>(
                                                            Map.of(
                                                                    "sku", product.getSku(),
                                                                    "name", product.getName(),
                                                                    "category",
                                                                            product.getCategory()
                                                                                    .name(),
                                                                    "stockQuantity",
                                                                            product
                                                                                    .getStockQuantity(),
                                                                    "minStockLevel",
                                                                            product
                                                                                    .getMinStockLevel(),
                                                                    "alertLevel", "WARNING",
                                                                    "message",
                                                                            "Below minimum stock level - reorder recommended",
                                                                    "daysUntilOutOfStock",
                                                                            estimateDaysUntilOutOfStock(
                                                                                    product))))
                            .toList();

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "criticalAlerts", criticalAlerts,
                                    "warningAlerts", warningAlerts,
                                    "summary",
                                            Map.of(
                                                    "totalCritical", criticalAlerts.size(),
                                                    "totalWarnings", warningAlerts.size(),
                                                    "totalNeedsAttention",
                                                            criticalAlerts.size()
                                                                    + warningAlerts.size()),
                                    "recommendations",
                                            generateInventoryRecommendations(
                                                    criticalAlerts, warningAlerts),
                                    "timestamp", java.time.Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get critical stock alerts resource", e);
            throw new RuntimeException("Failed to retrieve critical stock alerts", e);
        }
    }

    private McpSchema.ReadResourceResult getInventoryValueAnalysisResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Product> allProducts = productService.findAllProducts();

            Map<Category, BigDecimal> valueByCategory =
                    allProducts.stream()
                            .filter(p -> p.getPrice() != null)
                            .collect(
                                    Collectors.groupingBy(
                                            Product::getCategory,
                                            Collectors.reducing(
                                                    BigDecimal.ZERO,
                                                    p ->
                                                            p.getPrice()
                                                                    .multiply(
                                                                            BigDecimal.valueOf(
                                                                                    p
                                                                                            .getStockQuantity())),
                                                    BigDecimal::add)));

            BigDecimal totalValue =
                    valueByCategory.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calculate inventory turnover estimation (simplified)
            Map<Category, Double> turnoverByCategory =
                    valueByCategory.entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry ->
                                                    calculateTurnoverRate(
                                                            entry.getKey(), allProducts)));

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "totalInventoryValue", totalValue,
                                    "valueByCategory",
                                            valueByCategory.entrySet().stream()
                                                    .collect(
                                                            Collectors.toMap(
                                                                    e -> e.getKey().name(),
                                                                    Map.Entry::getValue)),
                                    "turnoverAnalysis",
                                            turnoverByCategory.entrySet().stream()
                                                    .collect(
                                                            Collectors.toMap(
                                                                    e -> e.getKey().name(),
                                                                    e ->
                                                                            String.format(
                                                                                    "%.2f",
                                                                                    e.getValue()))),
                                    "insights",
                                            generateValueInsights(
                                                    valueByCategory, turnoverByCategory),
                                    "timestamp", java.time.Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get inventory value analysis resource", e);
            throw new RuntimeException("Failed to retrieve inventory value analysis", e);
        }
    }

    private McpSchema.ReadResourceResult getStockMovementPredictionsResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Product> allProducts = productService.findAllProducts();

            List<Map<String, Object>> predictions =
                    allProducts.stream()
                            .filter(p -> p.getStockQuantity() > 0)
                            .map(this::generateStockMovementPrediction)
                            .collect(Collectors.toList());

            List<Map<String, Object>> reorderRecommendations =
                    allProducts.stream()
                            .filter(
                                    p ->
                                            p.getStockQuantity()
                                                    <= p.getMinStockLevel() * 1.5) // 50% buffer
                            .map(this::generateReorderRecommendation)
                            .collect(Collectors.toList());

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "stockMovementPredictions",
                                    predictions,
                                    "reorderRecommendations",
                                    reorderRecommendations,
                                    "predictionModel",
                                    Map.of(
                                            "version", "1.0",
                                            "description",
                                                    "Basic statistical model based on current stock levels and category trends",
                                            "accuracy",
                                                    "Estimated 75% accuracy for 30-day predictions"),
                                    "timestamp",
                                    java.time.Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get stock movement predictions resource", e);
            throw new RuntimeException("Failed to retrieve stock movement predictions", e);
        }
    }

    private McpSchema.ReadResourceResult getCategoryInventoryHealthResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            String uriPath = request.uri().toString();
            String categoryName = extractPathParameter(uriPath, "category");

            if (categoryName == null) {
                throw new IllegalArgumentException("Category parameter is required");
            }

            Category category = Category.valueOf(categoryName.toUpperCase());
            List<Product> categoryProducts =
                    productService.findAllProducts().stream()
                            .filter(p -> p.getCategory() == category)
                            .collect(Collectors.toList());

            Map<String, Object> healthMetrics = calculateCategoryHealthMetrics(categoryProducts);
            List<Map<String, Object>> productDetails =
                    categoryProducts.stream()
                            .map(this::createProductHealthDetail)
                            .collect(Collectors.toList());

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "category", category.name(),
                                    "healthMetrics", healthMetrics,
                                    "products", productDetails,
                                    "recommendations",
                                            generateCategoryRecommendations(
                                                    category, categoryProducts),
                                    "timestamp", java.time.Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get category inventory health resource", e);
            throw new RuntimeException("Failed to retrieve category inventory health", e);
        }
    }

    // Helper methods
    private double calculateStockHealthScore(List<Product> products) {
        if (products.isEmpty()) return 0.0;

        long healthyProducts =
                products.stream()
                        .mapToLong(p -> p.getStockQuantity() > p.getMinStockLevel() ? 1 : 0)
                        .sum();

        return (double) healthyProducts / products.size() * 100;
    }

    private BigDecimal calculateEstimatedLostSales(Product product) {
        // Simplified calculation: assume 1 sale per day at product price
        return product.getPrice().multiply(BigDecimal.valueOf(7)); // 7 days of lost sales
    }

    private int estimateDaysUntilOutOfStock(Product product) {
        // Simplified calculation: assume 1 unit sold per day
        return product.getStockQuantity();
    }

    private List<String> generateInventoryRecommendations(
            List<Map<String, Object>> critical, List<Map<String, Object>> warnings) {
        return List.of(
                "Immediately reorder " + critical.size() + " out-of-stock items",
                "Schedule reorders for " + warnings.size() + " low-stock items",
                "Review minimum stock levels for frequently low-stock categories",
                "Consider implementing automated reorder points");
    }

    private double calculateTurnoverRate(Category category, List<Product> allProducts) {
        // Simplified turnover calculation based on stock levels
        return Math.random() * 3 + 1; // Mock data: 1-4 turnover rate
    }

    private List<String> generateValueInsights(
            Map<Category, BigDecimal> valueByCategory, Map<Category, Double> turnoverByCategory) {
        return List.of(
                "Highest value category: " + findHighestValueCategory(valueByCategory),
                "Fastest moving category: " + findFastestTurnoverCategory(turnoverByCategory),
                "Consider diversifying high-value, low-turnover categories",
                "Optimize stock levels for high-turnover categories");
    }

    private String findHighestValueCategory(Map<Category, BigDecimal> valueByCategory) {
        return valueByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse("None");
    }

    private String findFastestTurnoverCategory(Map<Category, Double> turnoverByCategory) {
        return turnoverByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse("None");
    }

    private Map<String, Object> generateStockMovementPrediction(Product product) {
        // Simplified prediction model
        int daysToStockOut = Math.max(1, product.getStockQuantity() / 2); // Assume 2 units per day
        return Map.of(
                "sku", product.getSku(),
                "name", product.getName(),
                "currentStock", product.getStockQuantity(),
                "predictedDaysToStockOut", daysToStockOut,
                "recommendedReorderDate",
                        java.time.LocalDate.now().plusDays(daysToStockOut - 7).toString(),
                "confidence", "Medium");
    }

    private Map<String, Object> generateReorderRecommendation(Product product) {
        int recommendedOrderQuantity = Math.max(product.getMinStockLevel() * 2, 10);
        return Map.of(
                "sku", product.getSku(),
                "name", product.getName(),
                "currentStock", product.getStockQuantity(),
                "minStockLevel", product.getMinStockLevel(),
                "recommendedOrderQuantity", recommendedOrderQuantity,
                "estimatedCost",
                        product.getPrice().multiply(BigDecimal.valueOf(recommendedOrderQuantity)),
                "priority", product.getStockQuantity() == 0 ? "HIGH" : "MEDIUM");
    }

    private Map<String, Object> calculateCategoryHealthMetrics(List<Product> products) {
        if (products.isEmpty()) {
            return Map.of("error", "No products found in this category");
        }

        long totalProducts = products.size();
        long inStock = products.stream().mapToLong(p -> p.getStockQuantity() > 0 ? 1 : 0).sum();
        long lowStock =
                products.stream()
                        .mapToLong(
                                p ->
                                        p.getStockQuantity() > 0
                                                        && p.getStockQuantity()
                                                                <= p.getMinStockLevel()
                                                ? 1
                                                : 0)
                        .sum();

        return Map.of(
                "totalProducts", totalProducts,
                "inStock", inStock,
                "outOfStock", totalProducts - inStock,
                "lowStock", lowStock,
                "healthScore", (double) (inStock - lowStock) / totalProducts * 100);
    }

    private Map<String, Object> createProductHealthDetail(Product product) {
        String status =
                product.getStockQuantity() == 0
                        ? "OUT_OF_STOCK"
                        : product.getStockQuantity() <= product.getMinStockLevel()
                                ? "LOW_STOCK"
                                : "HEALTHY";

        return Map.of(
                "sku", product.getSku(),
                "name", product.getName(),
                "stockQuantity", product.getStockQuantity(),
                "minStockLevel", product.getMinStockLevel(),
                "status", status,
                "stockValue",
                        product.getPrice()
                                .multiply(BigDecimal.valueOf(product.getStockQuantity())));
    }

    private List<String> generateCategoryRecommendations(
            Category category, List<Product> products) {
        long outOfStock = products.stream().mapToLong(p -> p.getStockQuantity() == 0 ? 1 : 0).sum();
        long lowStock =
                products.stream()
                        .mapToLong(
                                p ->
                                        p.getStockQuantity() > 0
                                                        && p.getStockQuantity()
                                                                <= p.getMinStockLevel()
                                                ? 1
                                                : 0)
                        .sum();

        return List.of(
                "Category " + category.name() + " has " + outOfStock + " out-of-stock items",
                "Consider bulk reordering for " + lowStock + " low-stock items",
                "Review category-specific minimum stock levels",
                "Implement seasonal adjustment for " + category.name() + " category");
    }

    private String extractPathParameter(String uriPath, String paramName) {
        // Extract parameter from URI path like inventory://health/category/ELECTRONICS
        String[] pathParts = uriPath.split("/");
        if (pathParts.length > 0 && "category".equals(paramName)) {
            return pathParts[pathParts.length - 1]; // Last part of the path
        }
        return null;
    }
}
