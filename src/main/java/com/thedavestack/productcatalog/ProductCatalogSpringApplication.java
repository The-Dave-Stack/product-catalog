package com.thedavestack.productcatalog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.thedavestack.productcatalog.mcp.APIDocumentationResourceProvider;
import com.thedavestack.productcatalog.mcp.BusinessRulesResourceProvider;
import com.thedavestack.productcatalog.mcp.CategoryInformationResourceProvider;
import com.thedavestack.productcatalog.mcp.InventoryStatusResourceProvider;
import com.thedavestack.productcatalog.mcp.ProductCatalogResourceProvider;
import com.thedavestack.productcatalog.mcp.ProductManagementPromptProvider;
import com.thedavestack.productcatalog.service.ProductService;

import io.modelcontextprotocol.server.McpServerFeatures;

@SpringBootApplication
public class ProductCatalogSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogSpringApplication.class, args);
    }

    /**
     * Enhanced MCP Tools - Expose ProductService operations as intelligent AI tools with detailed
     * descriptions for better AI interaction and understanding.
     *
     * <p>Available Tools: - findAllProducts: Retrieve complete product catalog - findById: Get
     * specific product by ID - createProduct: Create new product with validation -
     * createMultipleProducts: Bulk product creation - updateProduct: Update existing product fields
     * - deleteProduct: Soft delete product (preserves data) - findAll (paginated): Products with
     * pagination support - findWithFilters: Advanced search with multiple criteria -
     * findLowStockProducts: Identify products needing reorder
     */
    @Bean
    public ToolCallbackProvider productCatalogTools(ProductService productService) {
        return MethodToolCallbackProvider.builder().toolObjects(productService).build();
    }

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> productCatalogResources(
            APIDocumentationResourceProvider aPIDocumentationResourceProvider,
            BusinessRulesResourceProvider businessRulesResourceProvider,
            CategoryInformationResourceProvider categoryInformationResourceProvider,
            InventoryStatusResourceProvider inventoryStatusResourceProvider,
            ProductCatalogResourceProvider productCatalogResourceProvider) {
        List<McpServerFeatures.SyncResourceSpecification> productCatalogResources =
                new ArrayList<>();
        productCatalogResources.addAll(
                aPIDocumentationResourceProvider.getResourceSpecifications());
        productCatalogResources.addAll(businessRulesResourceProvider.getResourceSpecifications());
        productCatalogResources.addAll(
                categoryInformationResourceProvider.getResourceSpecifications());
        productCatalogResources.addAll(inventoryStatusResourceProvider.getResourceSpecifications());
        productCatalogResources.addAll(productCatalogResourceProvider.getResourceSpecifications());
        return productCatalogResources;
    }

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> productCatalogPrompts(
            ProductManagementPromptProvider productManagementPromptProvider) {
        return productManagementPromptProvider.getPromptSpecifications();
    }
}
