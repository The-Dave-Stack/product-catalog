package com.thedavestack.productcatalog.mcp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedavestack.productcatalog.model.Category;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP Resource Provider for Business Rules and Validation Constraints. Provides comprehensive
 * business logic documentation, SKU generation rules, validation patterns, and compliance
 * requirements for AI-driven product management.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BusinessRulesResourceProvider {

    private final ObjectMapper objectMapper;

    /**
     * Provides MCP resource specifications for business rules information.
     *
     * @return List of synchronous resource specifications
     */
    public List<McpServerFeatures.SyncResourceSpecification> getResourceSpecifications() {
        return List.of(
                // SKU generation rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "business://rules/sku-generation",
                                "SKU Generation Rules",
                                "Comprehensive rules and patterns for generating unique Stock Keeping Units (SKUs)",
                                "application/json",
                                null),
                        this::getSKUGenerationRulesResource),

                // Product validation rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "business://rules/product-validation",
                                "Product Validation Rules",
                                "Complete validation rules and constraints for product data integrity",
                                "application/json",
                                null),
                        this::getProductValidationRulesResource),

                // Pricing rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "business://rules/pricing",
                                "Pricing Business Rules",
                                "Pricing strategies, constraints, and business logic for different product categories",
                                "application/json",
                                null),
                        this::getPricingRulesResource),

                // Inventory management rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "business://rules/inventory",
                                "Inventory Management Rules",
                                "Stock level management, reorder points, and inventory optimization rules",
                                "application/json",
                                null),
                        this::getInventoryRulesResource),

                // Compliance and regulatory rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "business://rules/compliance",
                                "Compliance & Regulatory Rules",
                                "Industry compliance requirements, safety standards, and regulatory constraints",
                                "application/json",
                                null),
                        this::getComplianceRulesResource),

                // Data quality rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "business://rules/data-quality",
                                "Data Quality Standards",
                                "Data quality requirements, completeness standards, and accuracy guidelines",
                                "application/json",
                                null),
                        this::getDataQualityRulesResource));
    }

    private McpSchema.ReadResourceResult getSKUGenerationRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> skuRules =
                    Map.of(
                            "overview",
                                    Map.of(
                                            "purpose",
                                                    "SKUs provide unique identification for each product variant",
                                            "format",
                                                    "Category-based prefix + sequential or custom identifier",
                                            "uniqueness",
                                                    "Must be globally unique within the system",
                                            "immutability",
                                                    "SKUs should not change once assigned to avoid confusion"),
                            "generationStrategies",
                                    Map.of(
                                            "automatic",
                                                    Map.of(
                                                            "description",
                                                            "System generates UUID-based SKU when none provided",
                                                            "format",
                                                            "UUID format (e.g., 550e8400-e29b-41d4-a716-446655440000)",
                                                            "advantages",
                                                            List.of(
                                                                    "Guaranteed uniqueness",
                                                                    "No collision risk",
                                                                    "Automatic generation"),
                                                            "disadvantages",
                                                            List.of(
                                                                    "Not human-readable",
                                                                    "No business meaning")),
                                            "categoryBased",
                                                    Map.of(
                                                            "description",
                                                                    "Human-readable SKUs with category prefixes",
                                                            "format",
                                                                    "{CATEGORY_CODE}-{SEQUENCE_NUMBER}",
                                                            "examples",
                                                                    Map.of(
                                                                            "ELECTRONICS",
                                                                                    "ELEC-001, ELEC-002, ELEC-SMARTPHONE-001",
                                                                            "BOOKS",
                                                                                    "BOOK-001, BOOK-FICTION-001, BOOK-TECH-001",
                                                                            "CLOTHING",
                                                                                    "CLOTH-001, CLOTH-MENS-001, CLOTH-WOMENS-001"),
                                                            "advantages",
                                                                    List.of(
                                                                            "Human readable",
                                                                            "Category grouping",
                                                                            "Business context"),
                                                            "disadvantages",
                                                                    List.of(
                                                                            "Requires sequence management",
                                                                            "Potential conflicts"))),
                            "categoryPrefixes",
                                    Arrays.stream(Category.values())
                                            .collect(
                                                    Collectors.toMap(
                                                            Category::name,
                                                            this::getCategorySkuPrefix)),
                            "validationRules",
                                    Map.of(
                                            "format",
                                                    Map.of(
                                                            "minLength", 3,
                                                            "maxLength", 50,
                                                            "allowedCharacters",
                                                                    "Alphanumeric, hyphens, underscores",
                                                            "pattern",
                                                                    "^[A-Z0-9][A-Z0-9_-]*[A-Z0-9]$",
                                                            "caseSensitivity",
                                                                    "Case-insensitive (stored as uppercase)"),
                                            "businessRules",
                                                    List.of(
                                                            "Must be unique across all products",
                                                            "Should be meaningful and descriptive when possible",
                                                            "Avoid special characters except hyphens and underscores",
                                                            "Consider future scalability in numbering schemes",
                                                            "Document SKU patterns for consistency")),
                            "bestPractices",
                                    List.of(
                                            "Use consistent category prefixes across products",
                                            "Implement logical numbering sequences",
                                            "Reserve blocks of numbers for different product lines",
                                            "Consider variant identification in complex products",
                                            "Maintain a SKU registry for tracking patterns",
                                            "Plan for international product codes if needed"),
                            "exampleImplementation", createSKUExamples(),
                            "migrationStrategy",
                                    Map.of(
                                            "existingProducts",
                                                    "Maintain existing SKUs for continuity",
                                            "newProducts", "Apply new SKU generation rules",
                                            "bulkUpdates",
                                                    "Use bulk operations for SKU standardization",
                                            "validation",
                                                    "Run validation checks before applying changes"));

            String jsonContent = objectMapper.writeValueAsString(skuRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get SKU generation rules resource", e);
            throw new RuntimeException("Failed to retrieve SKU generation rules", e);
        }
    }

    private McpSchema.ReadResourceResult getProductValidationRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> validationRules =
                    Map.of(
                            "fieldValidation",
                                    Map.of(
                                            "name",
                                                    Map.of(
                                                            "required",
                                                            true,
                                                            "minLength",
                                                            3,
                                                            "maxLength",
                                                            100,
                                                            "pattern",
                                                            "^[a-zA-Z0-9\\s\\-\\.\\,\\(\\)]+$",
                                                            "businessRules",
                                                            List.of(
                                                                    "Must be descriptive and customer-facing",
                                                                    "Should include key product features when relevant",
                                                                    "Avoid excessive capitalization",
                                                                    "Include brand name when appropriate")),
                                            "description",
                                                    Map.of(
                                                            "required",
                                                            false,
                                                            "minLength",
                                                            10,
                                                            "maxLength",
                                                            1000,
                                                            "businessRules",
                                                            List.of(
                                                                    "Should provide comprehensive product information",
                                                                    "Include key features and benefits",
                                                                    "Use customer-friendly language",
                                                                    "Mention compatibility or requirements when relevant")),
                                            "price",
                                                    Map.of(
                                                            "required",
                                                            true,
                                                            "minimum",
                                                            0.01,
                                                            "maximum",
                                                            99999.99,
                                                            "precision",
                                                            "2 decimal places",
                                                            "businessRules",
                                                            List.of(
                                                                    "Must reflect current market conditions",
                                                                    "Consider competitor pricing",
                                                                    "Account for cost basis and desired margins",
                                                                    "Special pricing requires approval for high-value items")),
                                            "stockQuantity",
                                                    Map.of(
                                                            "required",
                                                            true,
                                                            "minimum",
                                                            0,
                                                            "maximum",
                                                            999999,
                                                            "type",
                                                            "integer",
                                                            "businessRules",
                                                            List.of(
                                                                    "Negative stock indicates system errors",
                                                                    "Zero stock triggers out-of-stock workflows",
                                                                    "High stock levels may indicate overstock issues")),
                                            "minStockLevel",
                                                    Map.of(
                                                            "required",
                                                            true,
                                                            "minimum",
                                                            0,
                                                            "maximum",
                                                            1000,
                                                            "businessRules",
                                                            List.of(
                                                                    "Should account for lead times and demand variability",
                                                                    "Higher for fast-moving items",
                                                                    "Consider supplier reliability",
                                                                    "Review and adjust based on historical data"))),
                            "categorySpecificValidation",
                                    Arrays.stream(Category.values())
                                            .collect(
                                                    Collectors.toMap(
                                                            Category::name,
                                                            this::getCategorySpecificValidation)),
                            "crossFieldValidation",
                                    Map.of(
                                            "stockVsMinStock",
                                                    "Stock quantity should be monitored against minimum levels",
                                            "priceVsCategory",
                                                    "Pricing should align with category expectations",
                                            "descriptionVsName",
                                                    "Description should complement and expand on product name",
                                            "dimensionsVsWeight",
                                                    "Physical dimensions should be proportional to weight"),
                            "dataIntegrity",
                                    Map.of(
                                            "duplicateDetection",
                                                    List.of(
                                                            "Check for duplicate SKUs before creation",
                                                            "Monitor similar product names within categories",
                                                            "Validate unique constraints at database level"),
                                            "referentialIntegrity",
                                                    List.of(
                                                            "Ensure category values exist in enumeration",
                                                            "Validate foreign key relationships",
                                                            "Check audit log consistency")),
                            "errorHandling",
                                    Map.of(
                                            "validationFailures",
                                                    "Return specific field-level error messages",
                                            "businessRuleViolations",
                                                    "Provide clear business context for failures",
                                            "systemErrors",
                                                    "Log technical errors while showing user-friendly messages",
                                            "partialFailures",
                                                    "For bulk operations, report individual item failures"));

            String jsonContent = objectMapper.writeValueAsString(validationRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get product validation rules resource", e);
            throw new RuntimeException("Failed to retrieve product validation rules", e);
        }
    }

    private McpSchema.ReadResourceResult getPricingRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> pricingRules =
                    Map.of(
                            "pricingStrategies",
                                    Map.of(
                                            "competitivePricing",
                                                    Map.of(
                                                            "description",
                                                                    "Price based on competitor analysis",
                                                            "categories",
                                                                    List.of("ELECTRONICS", "BOOKS"),
                                                            "considerations",
                                                                    List.of(
                                                                            "Monitor competitor prices regularly",
                                                                            "Consider total value proposition, not just price",
                                                                            "Account for shipping and service differences")),
                                            "costPlusPricing",
                                                    Map.of(
                                                            "description",
                                                                    "Cost basis plus desired margin",
                                                            "categories",
                                                                    List.of(
                                                                            "AUTOMOTIVE",
                                                                            "HOME_GARDEN"),
                                                            "marginTargets",
                                                                    Map.of(
                                                                            "standard", "25-40%",
                                                                            "premium", "40-60%",
                                                                            "specialty", "50%+")),
                                            "valueBasedPricing",
                                                    Map.of(
                                                            "description",
                                                                    "Price based on customer perceived value",
                                                            "categories",
                                                                    List.of(
                                                                            "HEALTH_BEAUTY",
                                                                            "SPORTS"),
                                                            "factors",
                                                                    List.of(
                                                                            "Brand reputation and recognition",
                                                                            "Product uniqueness and features",
                                                                            "Customer willingness to pay",
                                                                            "Market positioning strategy"))),
                            "categoryPricingGuidelines",
                                    Arrays.stream(Category.values())
                                            .collect(
                                                    Collectors.toMap(
                                                            Category::name,
                                                            this::getCategoryPricingGuidelines)),
                            "dynamicPricing",
                                    Map.of(
                                            "factors",
                                                    List.of(
                                                            "Inventory levels (markdown for overstock)",
                                                            "Seasonality and demand cycles",
                                                            "Competitor price changes",
                                                            "Product lifecycle stage",
                                                            "Customer segment targeting"),
                                            "automation",
                                                    Map.of(
                                                            "rules",
                                                                    "Implement automated price adjustments for specific scenarios",
                                                            "limits",
                                                                    "Set minimum and maximum price bounds",
                                                            "approvals",
                                                                    "Require approval for significant price changes"),
                                            "monitoring",
                                                    "Track price elasticity and sales impact"),
                            "promotionalPricing",
                                    Map.of(
                                            "discountTypes",
                                                    Map.of(
                                                            "percentageDiscount",
                                                                    "5%-50% off regular price",
                                                            "fixedAmount",
                                                                    "Specific dollar amount off",
                                                            "bundleDiscounts",
                                                                    "Savings when buying multiple items",
                                                            "loyaltyDiscounts",
                                                                    "Special pricing for repeat customers"),
                                            "restrictions",
                                                    List.of(
                                                            "Minimum order quantities for bulk discounts",
                                                            "Time-limited promotional windows",
                                                            "Category or brand exclusions",
                                                            "Geographic or customer segment limitations")),
                            "pricingWorkflow",
                                    Map.of(
                                            "newProducts",
                                                    List.of(
                                                            "Research competitor pricing",
                                                            "Calculate cost basis and desired margins",
                                                            "Consider market positioning",
                                                            "Set initial price with review schedule"),
                                            "priceChanges",
                                                    List.of(
                                                            "Document reason for change",
                                                            "Analyze potential sales impact",
                                                            "Get required approvals",
                                                            "Implement with effective date",
                                                            "Monitor performance post-change")),
                            "complianceRequirements",
                                    List.of(
                                            "Maintain price change audit trail",
                                            "Ensure pricing transparency to customers",
                                            "Comply with minimum advertised price (MAP) agreements",
                                            "Follow industry-specific pricing regulations"));

            String jsonContent = objectMapper.writeValueAsString(pricingRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get pricing rules resource", e);
            throw new RuntimeException("Failed to retrieve pricing rules", e);
        }
    }

    private McpSchema.ReadResourceResult getInventoryRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> inventoryRules =
                    Map.of(
                            "stockLevelManagement",
                                    Map.of(
                                            "minimumStockLevels",
                                                    Map.of(
                                                            "calculation",
                                                                    "Based on lead time, demand variability, and service level targets",
                                                            "factors",
                                                                    List.of(
                                                                            "Average daily demand",
                                                                            "Supplier lead time reliability",
                                                                            "Demand forecast accuracy",
                                                                            "Desired service level (typically 95-99%)"),
                                                            "reviewFrequency",
                                                                    "Monthly for regular items, weekly for fast-movers"),
                                            "maximumStockLevels",
                                                    Map.of(
                                                            "purpose",
                                                                    "Prevent overstock and carrying cost issues",
                                                            "calculation",
                                                                    "Consider storage costs, product lifecycle, and cash flow",
                                                            "monitoring",
                                                                    "Flag items exceeding maximum levels for action")),
                            "reorderingRules",
                                    Map.of(
                                            "reorderPoints",
                                                    Map.of(
                                                            "trigger",
                                                                    "When stock quantity <= minimum stock level",
                                                            "leadTimeBuffer",
                                                                    "Include safety stock for supply chain variability",
                                                            "automation",
                                                                    "Implement automated reorder triggers when possible"),
                                            "orderQuantities",
                                                    Map.of(
                                                            "economicOrderQuantity",
                                                                    "Balance ordering costs with carrying costs",
                                                            "supplierMinimums",
                                                                    "Account for supplier minimum order quantities",
                                                            "volumeDiscounts",
                                                                    "Consider bulk purchase advantages",
                                                            "storageConstraints",
                                                                    "Physical storage capacity limitations")),
                            "categorySpecificRules",
                                    Arrays.stream(Category.values())
                                            .collect(
                                                    Collectors.toMap(
                                                            Category::name,
                                                            this::getCategoryInventoryRules)),
                            "stockAdjustments",
                                    Map.of(
                                            "reasons",
                                                    List.of(
                                                            "Physical count discrepancies",
                                                            "Damaged or defective goods",
                                                            "Returns and exchanges",
                                                            "Theft or loss",
                                                            "System errors or corrections"),
                                            "approvals",
                                                    Map.of(
                                                            "minor",
                                                                    "Adjustments under $100 - supervisor approval",
                                                            "significant",
                                                                    "Adjustments $100-$1000 - manager approval",
                                                            "major",
                                                                    "Adjustments over $1000 - executive approval"),
                                            "documentation",
                                                    "All adjustments require detailed documentation and audit trail"),
                            "inventoryAnalytics",
                                    Map.of(
                                            "turnoverRates",
                                                    Map.of(
                                                            "calculation",
                                                                    "Cost of goods sold / Average inventory value",
                                                            "targets",
                                                                    Map.of(
                                                                            "electronics",
                                                                                    "6-12 times per year",
                                                                            "clothing",
                                                                                    "4-6 times per year",
                                                                            "books",
                                                                                    "3-5 times per year"),
                                                            "actions",
                                                                    "Slow-moving items require price reduction or discontinuation"),
                                            "abcAnalysis",
                                                    Map.of(
                                                            "classA",
                                                                    "Top 20% of products by revenue - tight control",
                                                            "classB",
                                                                    "Next 30% of products - moderate control",
                                                            "classC",
                                                                    "Remaining 50% of products - loose control",
                                                            "management",
                                                                    "Focus resources on Class A items")),
                            "qualityControl",
                                    Map.of(
                                            "incomingInspection",
                                                    "Random sampling of received inventory",
                                            "storageConditions",
                                                    "Maintain appropriate environmental conditions",
                                            "expirationDates",
                                                    "Track and rotate inventory with expiration dates",
                                            "damageAssessment",
                                                    "Regular inspection for damaged goods"));

            String jsonContent = objectMapper.writeValueAsString(inventoryRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get inventory rules resource", e);
            throw new RuntimeException("Failed to retrieve inventory rules", e);
        }
    }

    private McpSchema.ReadResourceResult getComplianceRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> complianceRules =
                    Map.of(
                            "dataPrivacy",
                                    Map.of(
                                            "regulations", List.of("GDPR", "CCPA", "PIPEDA"),
                                            "requirements",
                                                    List.of(
                                                            "Customer consent for data collection",
                                                            "Right to data deletion and portability",
                                                            "Data breach notification procedures",
                                                            "Privacy policy transparency"),
                                            "implementation",
                                                    List.of(
                                                            "Anonymize customer data in analytics",
                                                            "Secure storage of personal information",
                                                            "Regular privacy impact assessments")),
                            "productSafety",
                                    Map.of(
                                            "standards",
                                                    Map.of(
                                                            "toys",
                                                                    "CPSC safety standards for children's products",
                                                            "electronics",
                                                                    "FCC certification for electronic devices",
                                                            "automotive",
                                                                    "DOT and FMVSS compliance for vehicle parts",
                                                            "healthBeauty",
                                                                    "FDA regulations for cosmetics and supplements"),
                                            "documentation",
                                                    List.of(
                                                            "Maintain certificates of compliance",
                                                            "Document supplier safety certifications",
                                                            "Track product recalls and safety notices")),
                            "taxCompliance",
                                    Map.of(
                                            "salesTax",
                                                    Map.of(
                                                            "nexus",
                                                                    "Understand tax obligations in operating jurisdictions",
                                                            "rates",
                                                                    "Maintain current tax rates for all locations",
                                                            "exemptions",
                                                                    "Handle tax-exempt customers and products"),
                                            "reporting",
                                                    List.of(
                                                            "Monthly sales tax returns",
                                                            "Annual tax reporting",
                                                            "Audit trail maintenance")),
                            "importExportRegulations",
                                    Map.of(
                                            "customs",
                                                    List.of(
                                                            "Proper product classification (HS codes)",
                                                            "Country of origin documentation",
                                                            "Import/export license requirements"),
                                            "restrictions",
                                                    List.of(
                                                            "Prohibited items by destination",
                                                            "Quantity limitations",
                                                            "Special handling requirements")),
                            "auditRequirements",
                                    Map.of(
                                            "internal",
                                                    List.of(
                                                            "Monthly inventory audits",
                                                            "Quarterly financial reconciliation",
                                                            "Annual compliance review"),
                                            "external",
                                                    List.of(
                                                            "Independent financial audits",
                                                            "Regulatory compliance audits",
                                                            "Supplier audits and certifications")),
                            "documentRetention",
                                    Map.of(
                                            "financial", "7 years for tax and financial records",
                                            "regulatory",
                                                    "Varies by regulation - typically 3-10 years",
                                            "audit", "Complete audit trail for all transactions",
                                            "disposal", "Secure destruction of expired documents"));

            String jsonContent = objectMapper.writeValueAsString(complianceRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get compliance rules resource", e);
            throw new RuntimeException("Failed to retrieve compliance rules", e);
        }
    }

    private McpSchema.ReadResourceResult getDataQualityRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> dataQualityRules =
                    Map.of(
                            "completeness",
                                    Map.of(
                                            "requiredFields",
                                                    List.of(
                                                            "All products must have name, price, category, and stock quantity",
                                                            "Product descriptions recommended for customer-facing items",
                                                            "Images required for online catalog display"),
                                            "optionalFields",
                                                    List.of(
                                                            "Weight and dimensions for shipping calculation",
                                                            "Brand information for filtering and search",
                                                            "Technical specifications for complex products"),
                                            "targets",
                                                    Map.of(
                                                            "required",
                                                                    "100% completion for mandatory fields",
                                                            "recommended",
                                                                    "90% completion for recommended fields",
                                                            "optional",
                                                                    "70% completion for optional fields")),
                            "accuracy",
                                    Map.of(
                                            "validation",
                                                    List.of(
                                                            "Price accuracy verified against supplier data",
                                                            "Product specifications match manufacturer data",
                                                            "Inventory counts reconciled with physical stock",
                                                            "Category assignments reviewed for correctness"),
                                            "sources",
                                                    List.of(
                                                            "Manufacturer product databases",
                                                            "Supplier catalogs and price lists",
                                                            "Industry standard classification systems",
                                                            "Customer feedback and returns data")),
                            "consistency",
                                    Map.of(
                                            "formatting",
                                                    List.of(
                                                            "Consistent naming conventions across similar products",
                                                            "Standardized measurement units (metric vs imperial)",
                                                            "Uniform price formatting and currency",
                                                            "Consistent category classifications"),
                                            "crossReference",
                                                    List.of(
                                                            "Product relationships maintained accurately",
                                                            "Supplier information synchronized",
                                                            "Inventory levels consistent across systems")),
                            "timeliness",
                                    Map.of(
                                            "updates",
                                                    Map.of(
                                                            "prices",
                                                                    "Updated within 24 hours of supplier changes",
                                                            "inventory",
                                                                    "Real-time updates for stock movements",
                                                            "product",
                                                                    "New product data within 48 hours of availability",
                                                            "discontinued",
                                                                    "Immediate marking of discontinued items"),
                                            "monitoring",
                                                    List.of(
                                                            "Track data freshness and update frequencies",
                                                            "Alert on stale data that exceeds thresholds",
                                                            "Monitor source system availability and sync status")),
                            "qualityMetrics",
                                    Map.of(
                                            "completenessRate",
                                                    "Percentage of required fields populated",
                                            "accuracyRate",
                                                    "Percentage of data matching authoritative sources",
                                            "consistencyRate",
                                                    "Percentage of data following standard formats",
                                            "timelinessRate",
                                                    "Percentage of data updated within SLA timeframes",
                                            "errorRate",
                                                    "Percentage of records with validation errors"),
                            "dataGovernance",
                                    Map.of(
                                            "ownership",
                                                    "Designated data stewards for each product category",
                                            "policies",
                                                    List.of(
                                                            "Data entry standards and procedures",
                                                            "Change management and approval workflows",
                                                            "Quality assurance and review processes"),
                                            "tools",
                                                    List.of(
                                                            "Automated validation rules and constraints",
                                                            "Data profiling and quality monitoring",
                                                            "Exception reporting and remediation workflows")),
                            "improvementProcess",
                                    Map.of(
                                            "monitoring",
                                                    "Continuous monitoring of data quality metrics",
                                            "reporting", "Regular quality reports to stakeholders",
                                            "remediation",
                                                    "Systematic process for addressing quality issues",
                                            "prevention",
                                                    "Root cause analysis to prevent recurring problems"));

            String jsonContent = objectMapper.writeValueAsString(dataQualityRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get data quality rules resource", e);
            throw new RuntimeException("Failed to retrieve data quality rules", e);
        }
    }

    // Helper methods for creating category-specific information
    private String getCategorySkuPrefix(Category category) {
        return switch (category) {
            case ELECTRONICS -> "ELEC";
            case BOOKS -> "BOOK";
            case CLOTHING -> "CLOTH";
            case HOME_GARDEN -> "HOME";
            case SPORTS_OUTDOORS -> "SPORT";
            case TOYS_GAMES -> "TOY";
            case HEALTH_BEAUTY -> "HEALTH";
            case FOOD_BEVERAGES -> "FOOD";
            case AUTOMOTIVE -> "AUTO";
            case OTHER -> "OTHER";
        };
    }

    private Map<String, Object> createSKUExamples() {
        return Map.of(
                "categoryBasedExamples",
                        Map.of(
                                "ELECTRONICS",
                                        List.of("ELEC-001", "ELEC-PHONE-001", "ELEC-LAPTOP-001"),
                                "BOOKS", List.of("BOOK-001", "BOOK-FICTION-001", "BOOK-TECH-001"),
                                "CLOTHING",
                                        List.of(
                                                "CLOTH-001",
                                                "CLOTH-SHIRT-M-001",
                                                "CLOTH-DRESS-L-001")),
                "automaticGeneration",
                        List.of(
                                "550e8400-e29b-41d4-a716-446655440000",
                                "6ba7b810-9dad-11d1-80b4-00c04fd430c8"),
                "customPatterns",
                        Map.of(
                                "seasonal", "XMAS-TOY-001, SUMMER-SPORT-001",
                                "branded", "APPLE-IPHONE-15-PRO, NIKE-AIR-MAX-001"));
    }

    private Map<String, Object> getCategorySpecificValidation(Category category) {
        return switch (category) {
            case ELECTRONICS -> Map.of(
                    "additionalFields", List.of("brand", "model", "warranty", "specifications"),
                    "priceRange", "$10 - $5000",
                    "specialValidation",
                            List.of(
                                    "Warranty period must be specified",
                                    "Technical specifications required for complex items"));
            case AUTOMOTIVE -> Map.of(
                    "additionalFields", List.of("partNumber", "compatibility", "manufacturer"),
                    "specialValidation",
                            List.of(
                                    "Vehicle compatibility must be specified",
                                    "OEM part numbers when available"));
            default -> Map.of(
                    "standardValidation",
                    "Standard validation rules apply",
                    "additionalFields",
                    List.of());
        };
    }

    private Map<String, Object> getCategoryPricingGuidelines(Category category) {
        return switch (category) {
            case ELECTRONICS -> Map.of(
                    "strategy", "Competitive pricing with tech lifecycle consideration",
                    "margin", "15-30%",
                    "considerations", List.of("Rapid price depreciation", "Technology cycles"));
            case BOOKS -> Map.of(
                    "strategy", "Standard retail pricing with educational discounts",
                    "margin", "40-50%",
                    "considerations", List.of("Publisher pricing", "Educational vs trade pricing"));
            default -> Map.of(
                    "strategy", "Category-appropriate competitive pricing",
                    "margin", "25-40%",
                    "considerations", List.of("Market competition", "Customer value perception"));
        };
    }

    private Map<String, Object> getCategoryInventoryRules(Category category) {
        return switch (category) {
            case ELECTRONICS -> Map.of(
                    "turnoverTarget",
                    "8-12 times per year",
                    "specialConsiderations",
                    List.of(
                            "Technology obsolescence risk",
                            "Seasonal demand patterns",
                            "New model introductions"));
            case CLOTHING -> Map.of(
                    "turnoverTarget",
                    "4-6 times per year",
                    "specialConsiderations",
                    List.of(
                            "Seasonal collections",
                            "Size distribution management",
                            "Fashion trend sensitivity"));
            default -> Map.of(
                    "turnoverTarget",
                    "6-8 times per year",
                    "specialConsiderations",
                    List.of("Standard inventory management"));
        };
    }
}
