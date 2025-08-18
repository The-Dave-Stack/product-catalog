package com.thedavestack.productcatalog.mcp;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP Prompt Provider for Product Management. Provides structured interaction templates and
 * guidance for AI-driven product catalog management operations.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductManagementPromptProvider {

    private final ObjectMapper objectMapper;

    /**
     * Provides MCP prompt specifications for product management operations.
     *
     * @return List of synchronous prompt specifications
     */
    public List<McpServerFeatures.SyncPromptSpecification> getPromptSpecifications() {
        return List.of(
                // Product creation guidance prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "product-creation-guide",
                                "Product Creation Guidance",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "category", "Product category", true),
                                        new McpSchema.PromptArgument(
                                                "complexity",
                                                "Product complexity level (simple/complex)",
                                                false))),
                        this::createProductCreationPrompt),

                // Inventory analysis prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "inventory-analysis",
                                "Inventory Analysis and Recommendations",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "analysisType",
                                                "Type of analysis (stock-health/reorder/turnover)",
                                                true),
                                        new McpSchema.PromptArgument(
                                                "category", "Focus on specific category", false))),
                        this::createInventoryAnalysisPrompt),

                // Pricing strategy prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "pricing-strategy",
                                "Pricing Strategy and Optimization",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "strategy",
                                                "Pricing strategy type (competitive/cost-plus/value-based)",
                                                true),
                                        new McpSchema.PromptArgument(
                                                "category",
                                                "Product category for pricing",
                                                false))),
                        this::createPricingStrategyPrompt),

                // Data quality audit prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "data-quality-audit",
                                "Data Quality Audit and Cleanup",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "scope",
                                                "Audit scope (completeness/accuracy/consistency)",
                                                true),
                                        new McpSchema.PromptArgument(
                                                "priority",
                                                "Priority level (high/medium/low)",
                                                false))),
                        this::createDataQualityAuditPrompt),

                // Troubleshooting assistant prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "troubleshooting-assistant",
                                "Product Management Troubleshooting",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "issue",
                                                "Issue type (api-error/data-inconsistency/business-rule)",
                                                true),
                                        new McpSchema.PromptArgument(
                                                "urgency",
                                                "Issue urgency level (critical/high/normal)",
                                                false))),
                        this::createTroubleshootingPrompt),

                // Business intelligence prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "business-intelligence",
                                "Business Intelligence and Insights",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "focus",
                                                "Analysis focus (performance/trends/optimization)",
                                                true),
                                        new McpSchema.PromptArgument(
                                                "timeframe",
                                                "Analysis timeframe (daily/weekly/monthly/quarterly)",
                                                false))),
                        this::createBusinessIntelligencePrompt),

                // Bulk operations prompt
                new McpServerFeatures.SyncPromptSpecification(
                        new McpSchema.Prompt(
                                "bulk-operations",
                                "Bulk Product Operations Planning",
                                List.of(
                                        new McpSchema.PromptArgument(
                                                "operation",
                                                "Bulk operation type (import/update/export/cleanup)",
                                                true),
                                        new McpSchema.PromptArgument(
                                                "scope",
                                                "Operation scope (category/price-range/all)",
                                                false))),
                        this::createBulkOperationsPrompt));
    }

    private McpSchema.GetPromptResult createProductCreationPrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String category = (String) request.arguments().getOrDefault("category", "GENERAL");
            String complexity = (String) request.arguments().getOrDefault("complexity", "simple");

            String promptText = createProductCreationPromptText(category, complexity);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Product creation guidance for "
                            + category
                            + " category ("
                            + complexity
                            + " complexity)",
                    List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create product creation prompt", e);
            throw new RuntimeException("Failed to generate product creation prompt", e);
        }
    }

    private McpSchema.GetPromptResult createInventoryAnalysisPrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String analysisType = (String) request.arguments().get("analysisType");
            String category = (String) request.arguments().getOrDefault("category", "ALL");

            String promptText = createInventoryAnalysisPromptText(analysisType, category);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Inventory analysis guidance for " + analysisType + " analysis",
                    List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create inventory analysis prompt", e);
            throw new RuntimeException("Failed to generate inventory analysis prompt", e);
        }
    }

    private McpSchema.GetPromptResult createPricingStrategyPrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String strategy = (String) request.arguments().get("strategy");
            String category = (String) request.arguments().getOrDefault("category", "ALL");

            String promptText = createPricingStrategyPromptText(strategy, category);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Pricing strategy guidance for " + strategy + " approach",
                    List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create pricing strategy prompt", e);
            throw new RuntimeException("Failed to generate pricing strategy prompt", e);
        }
    }

    private McpSchema.GetPromptResult createDataQualityAuditPrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String scope = (String) request.arguments().get("scope");
            String priority = (String) request.arguments().getOrDefault("priority", "medium");

            String promptText = createDataQualityAuditPromptText(scope, priority);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Data quality audit guidance for " + scope + " scope", List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create data quality audit prompt", e);
            throw new RuntimeException("Failed to generate data quality audit prompt", e);
        }
    }

    private McpSchema.GetPromptResult createTroubleshootingPrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String issue = (String) request.arguments().get("issue");
            String urgency = (String) request.arguments().getOrDefault("urgency", "normal");

            String promptText = createTroubleshootingPromptText(issue, urgency);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Troubleshooting guidance for " + issue + " issues", List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create troubleshooting prompt", e);
            throw new RuntimeException("Failed to generate troubleshooting prompt", e);
        }
    }

    private McpSchema.GetPromptResult createBusinessIntelligencePrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String focus = (String) request.arguments().get("focus");
            String timeframe = (String) request.arguments().getOrDefault("timeframe", "monthly");

            String promptText = createBusinessIntelligencePromptText(focus, timeframe);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Business intelligence guidance for " + focus + " analysis",
                    List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create business intelligence prompt", e);
            throw new RuntimeException("Failed to generate business intelligence prompt", e);
        }
    }

    private McpSchema.GetPromptResult createBulkOperationsPrompt(
            McpSyncServerExchange exchange, McpSchema.GetPromptRequest request) {
        try {
            String operation = (String) request.arguments().get("operation");
            String scope = (String) request.arguments().getOrDefault("scope", "all");

            String promptText = createBulkOperationsPromptText(operation, scope);

            McpSchema.PromptMessage promptMessage =
                    new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT, new McpSchema.TextContent(promptText));

            return new McpSchema.GetPromptResult(
                    "Bulk operations guidance for " + operation + " operations",
                    List.of(promptMessage));
        } catch (Exception e) {
            log.error("Failed to create bulk operations prompt", e);
            throw new RuntimeException("Failed to generate bulk operations prompt", e);
        }
    }

    // Prompt text creation methods
    private String createProductCreationPromptText(String category, String complexity) {
        String baseTemplate =
                """
                You are an expert product catalog manager helping to create a new product in the %s category.

                ## Product Creation Guidelines for %s Category

                ### Required Information:
                - Product name (descriptive and customer-friendly)
                - Detailed description highlighting key features
                - Competitive pricing based on market research
                - Appropriate category classification
                - Initial stock quantity and minimum stock level

                ### %s Category Specific Considerations:
                %s

                ### %s Complexity Guidelines:
                %s

                ### Best Practices:
                1. Research competitor products for pricing and positioning
                2. Ensure SKU follows category naming conventions
                3. Include all relevant product specifications
                4. Set appropriate minimum stock levels based on demand
                5. Consider seasonal factors in inventory planning

                ### Validation Checklist:
                - [ ] Product name is descriptive and unique
                - [ ] Price is competitive and profitable
                - [ ] Category assignment is accurate
                - [ ] Stock levels are realistic
                - [ ] Required fields are complete

                Please provide step-by-step guidance for creating this product effectively.
                """;

        String categoryConsiderations = getCategorySpecificConsiderations(category);
        String complexityGuidelines = getComplexityGuidelines(complexity);

        return String.format(
                baseTemplate,
                category,
                category,
                category,
                categoryConsiderations,
                complexity,
                complexityGuidelines);
    }

    private String createInventoryAnalysisPromptText(String analysisType, String category) {
        return switch (analysisType) {
            case "stock-health" -> """
                You are an inventory management specialist conducting a stock health analysis%s.

                ## Stock Health Analysis Framework

                ### Key Metrics to Evaluate:
                1. **Out of Stock Items** - Immediate attention required
                2. **Low Stock Warnings** - Items below minimum levels
                3. **Overstock Situations** - Items with excessive inventory
                4. **Stock Turnover Rates** - How quickly inventory moves
                5. **Dead Stock** - Items with no recent sales

                ### Analysis Process:
                1. Retrieve current inventory levels using available tools
                2. Compare against minimum stock thresholds
                3. Identify critical alerts requiring immediate action
                4. Calculate stock health scores by category
                5. Generate actionable recommendations

                ### Recommended Actions:
                - **Critical**: Immediate reorders for out-of-stock items
                - **Warning**: Schedule reorders for low-stock items
                - **Optimization**: Review slow-moving inventory for discounts
                - **Planning**: Adjust minimum stock levels based on trends

                Please conduct a comprehensive stock health analysis and provide prioritized recommendations.
                """
                    .formatted(category.equals("ALL") ? "" : " for " + category + " category");

            case "reorder" -> """
                You are a procurement specialist developing reorder recommendations%s.

                ## Reorder Analysis Framework

                ### Reorder Triggers:
                1. Current stock ≤ minimum stock level
                2. Projected stockouts based on demand trends
                3. Lead time considerations
                4. Economic order quantities
                5. Seasonal demand adjustments

                ### Analysis Steps:
                1. Identify products below reorder points
                2. Calculate optimal order quantities
                3. Consider supplier minimums and volume discounts
                4. Factor in lead times and safety stock
                5. Prioritize based on sales velocity and margin

                ### Reorder Recommendations:
                - High priority: Fast-moving items near stockout
                - Medium priority: Steady sellers below minimum
                - Low priority: Slow movers with adequate runway

                Please generate a prioritized reorder plan with specific quantities and timing.
                """
                    .formatted(category.equals("ALL") ? "" : " for " + category + " category");

            case "turnover" -> """
                You are a financial analyst evaluating inventory turnover performance%s.

                ## Inventory Turnover Analysis

                ### Key Calculations:
                1. **Turnover Ratio** = Cost of Goods Sold / Average Inventory Value
                2. **Days Sales Inventory** = 365 / Turnover Ratio
                3. **Stock Velocity** = Units Sold / Average Units in Stock
                4. **Carrying Cost Impact** = Average Inventory × Carrying Cost Rate

                ### Performance Benchmarks:
                - Electronics: 8-12 turns/year (Fast-moving technology)
                - Books: 3-5 turns/year (Diverse demand patterns)
                - Clothing: 4-6 turns/year (Seasonal collections)
                - General: 6-8 turns/year (Standard retail)

                ### Analysis Focus:
                1. Compare actual vs. target turnover rates
                2. Identify slow-moving inventory
                3. Calculate carrying cost implications
                4. Recommend optimization strategies

                Please analyze turnover performance and suggest improvements for capital efficiency.
                """
                    .formatted(category.equals("ALL") ? "" : " for " + category + " category");

            default -> "Please specify analysis type: stock-health, reorder, or turnover.";
        };
    }

    private String createPricingStrategyPromptText(String strategy, String category) {
        return switch (strategy) {
            case "competitive" -> """
                You are a pricing strategist implementing competitive pricing%s.

                ## Competitive Pricing Strategy

                ### Market Research Process:
                1. Identify key competitors in the market segment
                2. Gather competitor pricing data for similar products
                3. Analyze value proposition differences
                4. Consider total cost of ownership for customers
                5. Factor in brand positioning and market perception

                ### Pricing Analysis Framework:
                - **Price Matching**: Match competitor prices for commodity items
                - **Premium Pricing**: Justify higher prices with superior value
                - **Penetration Pricing**: Undercut competitors to gain market share
                - **Value Positioning**: Price based on unique value proposition

                ### Implementation Guidelines:
                1. Monitor competitor prices regularly
                2. Adjust prices based on market movements
                3. Maintain minimum margin requirements
                4. Consider dynamic pricing for high-velocity items
                5. Document pricing rationale for consistency

                Please develop a competitive pricing strategy with specific recommendations.
                """
                    .formatted(category.equals("ALL") ? "" : " for " + category + " category");

            case "cost-plus" -> """
                You are a financial analyst implementing cost-plus pricing%s.

                ## Cost-Plus Pricing Strategy

                ### Cost Calculation Components:
                1. **Direct Costs**: Product acquisition cost, shipping, handling
                2. **Indirect Costs**: Storage, insurance, shrinkage, overhead
                3. **Operating Costs**: Labor, utilities, technology, facilities
                4. **Opportunity Costs**: Capital cost, alternative investments

                ### Margin Structure:
                - **Standard Products**: 25-40% gross margin
                - **Premium Products**: 40-60% gross margin
                - **Specialty Items**: 50%+ gross margin
                - **High-Volume**: Lower margins with scale benefits

                ### Pricing Process:
                1. Calculate true product cost including all factors
                2. Determine appropriate margin based on category and competition
                3. Set price point that achieves target margin
                4. Monitor margin performance and adjust as needed

                Please develop a cost-plus pricing model with detailed margin analysis.
                """
                    .formatted(category.equals("ALL") ? "" : " for " + category + " category");

            case "value-based" -> """
                You are a product marketing strategist implementing value-based pricing%s.

                ## Value-Based Pricing Strategy

                ### Value Assessment Framework:
                1. **Customer Benefits**: Quantifiable value delivered to customers
                2. **Problem Solving**: How effectively the product solves customer problems
                3. **Differentiation**: Unique features and capabilities vs. alternatives
                4. **Brand Value**: Premium associated with brand reputation
                5. **Customer Segments**: Willingness to pay by customer type

                ### Value Quantification:
                - Economic value: Time savings, efficiency gains, cost reductions
                - Functional value: Performance improvements, reliability, convenience
                - Emotional value: Status, satisfaction, peace of mind
                - Social value: Environmental impact, social responsibility

                ### Pricing Implementation:
                1. Research customer willingness to pay
                2. Test price sensitivity through market research
                3. Segment customers by value perception
                4. Implement tiered pricing based on value segments
                5. Communicate value proposition effectively

                Please develop a value-based pricing strategy with customer segment analysis.
                """
                    .formatted(category.equals("ALL") ? "" : " for " + category + " category");

            default -> "Please specify pricing strategy: competitive, cost-plus, or value-based.";
        };
    }

    private String createDataQualityAuditPromptText(String scope, String priority) {
        return switch (scope) {
            case "completeness" -> """
                You are a data quality specialist conducting a completeness audit (%s priority).

                ## Data Completeness Audit Framework

                ### Completeness Metrics:
                1. **Required Fields**: 100% completion target
                   - Product name, price, category, stock quantity
                2. **Recommended Fields**: 90% completion target
                   - Description, images, specifications
                3. **Optional Fields**: 70% completion target
                   - Weight, dimensions, brand details

                ### Audit Process:
                1. Query product database for field population rates
                2. Identify products with missing critical information
                3. Prioritize fixes based on business impact
                4. Generate improvement recommendations
                5. Track completion rates over time

                ### %s Priority Actions:
                %s

                ### Remediation Strategies:
                - Bulk data import from supplier catalogs
                - Automated field population where possible
                - Manual data entry for high-value products
                - Quality gates for new product creation

                Please conduct a comprehensive completeness audit with actionable improvement plan.
                """
                    .formatted(priority, priority, getPriorityActions(priority, "completeness"));

            case "accuracy" -> """
                You are a data quality specialist conducting an accuracy audit (%s priority).

                ## Data Accuracy Audit Framework

                ### Accuracy Validation Points:
                1. **Price Accuracy**: Compare against supplier data and market rates
                2. **Product Specifications**: Verify against manufacturer information
                3. **Inventory Counts**: Reconcile with physical stock
                4. **Category Classifications**: Ensure proper categorization
                5. **Cross-References**: Validate relationships and dependencies

                ### Validation Process:
                1. Sample products for manual verification
                2. Cross-reference with authoritative sources
                3. Identify systematic data quality issues
                4. Calculate accuracy rates by data element
                5. Root cause analysis for inaccuracies

                ### %s Priority Focus:
                %s

                ### Accuracy Improvement:
                - Automated validation rules and constraints
                - Regular data synchronization with sources
                - Exception reporting for data anomalies
                - Staff training on data entry standards

                Please conduct a thorough accuracy audit with corrective action plan.
                """
                    .formatted(priority, priority, getPriorityActions(priority, "accuracy"));

            case "consistency" -> """
                You are a data quality specialist conducting a consistency audit (%s priority).

                ## Data Consistency Audit Framework

                ### Consistency Standards:
                1. **Naming Conventions**: Consistent product naming patterns
                2. **Format Standards**: Uniform data formats and units
                3. **Classification**: Consistent category assignments
                4. **Cross-System**: Data synchronization across systems
                5. **Temporal**: Historical data consistency over time

                ### Audit Areas:
                1. Product name variations for similar items
                2. Inconsistent measurement units (metric vs imperial)
                3. Price format variations and currency consistency
                4. Category classification discrepancies
                5. Duplicate product entries

                ### %s Priority Items:
                %s

                ### Standardization Process:
                - Develop data governance standards
                - Implement standardization rules
                - Create data quality monitoring dashboards
                - Establish regular consistency reviews

                Please conduct a consistency audit with standardization recommendations.
                """
                    .formatted(priority, priority, getPriorityActions(priority, "consistency"));

            default -> "Please specify audit scope: completeness, accuracy, or consistency.";
        };
    }

    private String createTroubleshootingPromptText(String issue, String urgency) {
        String urgencyPrefix =
                switch (urgency) {
                    case "critical" -> "🔴 CRITICAL ISSUE";
                    case "high" -> "🟡 HIGH PRIORITY";
                    default -> "🟢 NORMAL PRIORITY";
                };

        return switch (issue) {
            case "api-error" -> """
                %s - API Error Troubleshooting Guide

                ## Common API Issues and Solutions

                ### Authentication Errors (401):
                1. **Check JWT Token**: Verify token format and expiration
                2. **Credential Validation**: Confirm username/password accuracy
                3. **Role Permissions**: Ensure user has required role (ADMIN/USER)
                4. **Token Refresh**: Implement automatic token refresh logic

                ### Authorization Errors (403):
                1. **Permission Check**: Verify user role matches endpoint requirements
                2. **Resource Access**: Confirm access to specific product/resource
                3. **Rate Limiting**: Check if rate limits are being exceeded

                ### Data Errors (400, 422):
                1. **Request Format**: Validate JSON structure and content-type
                2. **Field Validation**: Check required fields and data types
                3. **Business Rules**: Ensure data meets business constraints
                4. **SKU Conflicts**: Check for duplicate SKU issues

                ### System Errors (500):
                1. **Log Analysis**: Check application logs for stack traces
                2. **Database Connectivity**: Verify database connection health
                3. **Resource Constraints**: Monitor memory and CPU usage
                4. **Dependency Issues**: Check external service availability

                ### Diagnostic Steps:
                1. Reproduce the error with detailed logging
                2. Check error response format and codes
                3. Verify request parameters and payload
                4. Test with different user roles and data
                5. Review recent system changes

                Please provide specific error details for targeted troubleshooting assistance.
                """
                    .formatted(urgencyPrefix);

            case "data-inconsistency" -> """
                %s - Data Inconsistency Troubleshooting

                ## Data Consistency Issue Resolution

                ### Common Inconsistency Types:
                1. **Inventory Discrepancies**: System vs physical stock differences
                2. **Price Mismatches**: Multiple price sources showing different values
                3. **Product Duplicates**: Same product with different SKUs/IDs
                4. **Category Misalignment**: Products in wrong categories
                5. **Stale Data**: Outdated information not refreshed

                ### Investigation Process:
                1. **Identify Scope**: Determine affected products/categories
                2. **Source Analysis**: Check data source systems and sync status
                3. **Timeline Review**: When did inconsistency first appear
                4. **Impact Assessment**: How many records are affected
                5. **Root Cause**: What caused the inconsistency

                ### Resolution Strategies:
                - **Data Reconciliation**: Compare and sync authoritative sources
                - **Automated Cleanup**: Run data correction scripts
                - **Manual Review**: Individual record verification and correction
                - **Process Improvement**: Prevent future inconsistencies

                ### Prevention Measures:
                1. Implement data validation rules
                2. Set up automated consistency monitoring
                3. Establish data governance procedures
                4. Regular data quality audits

                Please provide details about the specific data inconsistency for targeted resolution.
                """
                    .formatted(urgencyPrefix);

            case "business-rule" -> """
                %s - Business Rule Violation Troubleshooting

                ## Business Rule Enforcement Issues

                ### Common Rule Violations:
                1. **SKU Uniqueness**: Duplicate SKU assignment attempts
                2. **Price Constraints**: Prices outside acceptable ranges
                3. **Inventory Rules**: Stock levels violating business logic
                4. **Category Rules**: Invalid category assignments
                5. **Validation Logic**: Custom business rule failures

                ### Rule Categories:
                - **Data Integrity**: Ensure data consistency and accuracy
                - **Business Logic**: Enforce company policies and procedures
                - **Regulatory Compliance**: Meet legal and industry requirements
                - **Performance**: Maintain system efficiency and reliability

                ### Troubleshooting Steps:
                1. **Rule Identification**: Which specific rule is being violated
                2. **Context Analysis**: Why is the rule triggering
                3. **Data Examination**: What data is causing the violation
                4. **Exception Handling**: Is this a valid exception case
                5. **Resolution Path**: How to resolve while maintaining compliance

                ### Resolution Options:
                - **Data Correction**: Fix the data to comply with rules
                - **Rule Adjustment**: Modify rules if business needs changed
                - **Exception Process**: Handle legitimate exceptions properly
                - **Process Improvement**: Prevent future violations

                Please describe the specific business rule violation for detailed guidance.
                """
                    .formatted(urgencyPrefix);

            default -> "Please specify issue type: api-error, data-inconsistency, or business-rule.";
        };
    }

    private String createBusinessIntelligencePromptText(String focus, String timeframe) {
        return switch (focus) {
            case "performance" -> """
                You are a business analyst conducting performance analysis for %s timeframe.

                ## Performance Analysis Framework

                ### Key Performance Indicators:
                1. **Sales Performance**
                   - Revenue by product/category
                   - Units sold and growth trends
                   - Top performing products
                   - Underperforming items

                2. **Inventory Performance**
                   - Turnover rates by category
                   - Stock-out frequency
                   - Overstock situations
                   - Carrying cost efficiency

                3. **Operational Metrics**
                   - Order fulfillment rates
                   - Customer satisfaction scores
                   - Return/exchange rates
                   - Operational cost per unit

                ### %s Analysis Focus:
                %s

                ### Analytical Approach:
                1. Gather performance data using available tools
                2. Calculate key metrics and ratios
                3. Compare against targets and benchmarks
                4. Identify trends and patterns
                5. Generate actionable insights

                ### Deliverables:
                - Performance dashboard with key metrics
                - Trend analysis with visual representations
                - Benchmark comparisons and gap analysis
                - Improvement recommendations with priorities

                Please conduct comprehensive performance analysis with strategic recommendations.
                """
                    .formatted(timeframe, timeframe, getTimeframeConsiderations(timeframe));

            case "trends" -> """
                You are a market analyst identifying trends for %s timeframe.

                ## Trend Analysis Framework

                ### Trend Categories:
                1. **Sales Trends**
                   - Product demand patterns
                   - Seasonal variations
                   - Category growth/decline
                   - Customer preference shifts

                2. **Market Trends**
                   - Competitive landscape changes
                   - Price movement patterns
                   - New product introductions
                   - Market share dynamics

                3. **Inventory Trends**
                   - Stock level fluctuations
                   - Turnover rate changes
                   - Procurement pattern shifts
                   - Storage utilization trends

                ### %s Trend Focus:
                %s

                ### Analysis Methodology:
                1. Historical data pattern analysis
                2. Statistical trend identification
                3. Seasonal adjustment and normalization
                4. Predictive modeling and forecasting
                5. Market intelligence integration

                ### Strategic Implications:
                - Product portfolio optimization
                - Inventory planning adjustments
                - Pricing strategy refinements
                - Resource allocation decisions

                Please provide comprehensive trend analysis with forward-looking insights.
                """
                    .formatted(timeframe, timeframe, getTimeframeConsiderations(timeframe));

            case "optimization" -> """
                You are an operations analyst focusing on optimization for %s timeframe.

                ## Optimization Analysis Framework

                ### Optimization Areas:
                1. **Inventory Optimization**
                   - Stock level optimization
                   - Reorder point calibration
                   - ABC analysis implementation
                   - Dead stock elimination

                2. **Pricing Optimization**
                   - Price elasticity analysis
                   - Competitive positioning
                   - Margin optimization
                   - Dynamic pricing opportunities

                3. **Product Mix Optimization**
                   - High-margin product focus
                   - Low-performing product review
                   - Cross-selling opportunities
                   - Category balance assessment

                ### %s Optimization Priorities:
                %s

                ### Optimization Process:
                1. Current state analysis and baseline establishment
                2. Opportunity identification and quantification
                3. Optimization scenario modeling
                4. Implementation planning and resource requirements
                5. Success metrics and monitoring framework

                ### Expected Outcomes:
                - Improved profitability and margins
                - Enhanced inventory efficiency
                - Better resource utilization
                - Increased customer satisfaction

                Please develop comprehensive optimization strategy with implementation roadmap.
                """
                    .formatted(timeframe, timeframe, getTimeframeConsiderations(timeframe));

            default -> "Please specify focus area: performance, trends, or optimization.";
        };
    }

    private String createBulkOperationsPromptText(String operation, String scope) {
        return switch (operation) {
            case "import" -> """
                You are a data management specialist planning bulk product import for %s scope.

                ## Bulk Import Planning Framework

                ### Pre-Import Preparation:
                1. **Data Format Validation**
                   - CSV/Excel file structure verification
                   - Required field completeness check
                   - Data type and format validation
                   - SKU uniqueness verification

                2. **Business Rule Compliance**
                   - Category assignment validation
                   - Price range verification
                   - Inventory level reasonableness
                   - Description quality standards

                3. **System Readiness**
                   - Database capacity planning
                   - Performance impact assessment
                   - Backup and rollback strategy
                   - Error handling procedures

                ### Import Process:
                1. **Validation Phase**: Check all data before import
                2. **Staging Phase**: Load to temporary staging area
                3. **Verification Phase**: Verify staged data accuracy
                4. **Production Phase**: Move to production database
                5. **Confirmation Phase**: Validate successful import

                ### %s Scope Considerations:
                %s

                ### Quality Assurance:
                - Sample validation on subset before full import
                - Duplicate detection and resolution
                - Error logging and exception handling
                - Post-import data integrity checks

                Please provide detailed import plan with validation checklist.
                """
                    .formatted(scope, scope, getScopeConsiderations(scope));

            case "update" -> """
                You are a data management specialist planning bulk product updates for %s scope.

                ## Bulk Update Planning Framework

                ### Update Categories:
                1. **Price Updates**
                   - Percentage-based adjustments
                   - Fixed amount changes
                   - Competitive price matching
                   - Margin-based repricing

                2. **Inventory Updates**
                   - Stock quantity adjustments
                   - Minimum stock level changes
                   - Bulk stock corrections
                   - Seasonal inventory planning

                3. **Content Updates**
                   - Description enhancements
                   - Category reassignments
                   - Product specification updates
                   - Image and media updates

                ### Update Process:
                1. **Selection Criteria**: Define products for update
                2. **Change Specification**: Detail exact changes needed
                3. **Impact Analysis**: Assess business impact
                4. **Backup Strategy**: Ensure data recovery capability
                5. **Execution Plan**: Step-by-step update process

                ### %s Scope Strategy:
                %s

                ### Risk Management:
                - Staged rollout with monitoring
                - Automated rollback capabilities
                - Change approval workflows
                - Performance impact monitoring

                Please develop comprehensive bulk update strategy with risk mitigation.
                """
                    .formatted(scope, scope, getScopeConsiderations(scope));

            case "export" -> """
                You are a data analyst planning bulk product export for %s scope.

                ## Bulk Export Planning Framework

                ### Export Purposes:
                1. **Reporting and Analytics**
                   - Business intelligence reports
                   - Performance analysis data
                   - Inventory status exports
                   - Financial reporting

                2. **System Integration**
                   - ERP system synchronization
                   - E-commerce platform updates
                   - Third-party marketplace feeds
                   - Partner system integration

                3. **Backup and Archival**
                   - Complete data backup
                   - Historical data preservation
                   - Compliance documentation
                   - Disaster recovery preparation

                ### Export Specifications:
                1. **Data Selection**: Which products/fields to include
                2. **Format Requirements**: CSV, JSON, XML, or custom format
                3. **Transformation Rules**: Data formatting and calculation
                4. **Security Considerations**: Sensitive data handling
                5. **Delivery Method**: File download, API, or automated transfer

                ### %s Scope Requirements:
                %s

                ### Quality Assurance:
                - Data completeness verification
                - Format validation and testing
                - Security and privacy compliance
                - Performance optimization for large exports

                Please create detailed export specification with quality controls.
                """
                    .formatted(scope, scope, getScopeConsiderations(scope));

            case "cleanup" -> """
                You are a data quality specialist planning bulk data cleanup for %s scope.

                ## Bulk Cleanup Planning Framework

                ### Cleanup Categories:
                1. **Duplicate Removal**
                   - Identify duplicate products
                   - Merge or remove duplicates
                   - Update references and relationships
                   - Maintain data integrity

                2. **Data Standardization**
                   - Consistent naming conventions
                   - Format standardization
                   - Unit of measure normalization
                   - Category cleanup

                3. **Quality Enhancement**
                   - Missing data completion
                   - Inaccurate data correction
                   - Outdated information updates
                   - Description improvements

                ### Cleanup Process:
                1. **Assessment Phase**: Identify cleanup needs
                2. **Planning Phase**: Develop cleanup strategy
                3. **Validation Phase**: Test cleanup procedures
                4. **Execution Phase**: Implement cleanup operations
                5. **Verification Phase**: Confirm cleanup success

                ### %s Scope Priorities:
                %s

                ### Success Metrics:
                - Data quality score improvements
                - Duplicate reduction percentage
                - Standardization compliance rate
                - Error reduction metrics

                Please develop comprehensive cleanup plan with measurable outcomes.
                """
                    .formatted(scope, scope, getScopeConsiderations(scope));

            default -> "Please specify operation type: import, update, export, or cleanup.";
        };
    }

    // Helper methods for prompt customization
    private String getCategorySpecificConsiderations(String category) {
        return switch (category.toUpperCase()) {
            case "ELECTRONICS" -> """
                - Include technical specifications and compatibility information
                - Consider warranty terms and support requirements
                - Account for rapid technology obsolescence in pricing
                - Ensure compliance with FCC and safety regulations
                """;
            case "BOOKS" -> """
                - Verify ISBN accuracy and edition details
                - Include author, publisher, and publication information
                - Consider educational vs. trade market pricing
                - Account for format variations (hardcover, paperback, digital)
                """;
            case "CLOTHING" -> """
                - Include size charts and measurement guides
                - Specify material composition and care instructions
                - Consider seasonal collections and fashion cycles
                - Account for size distribution in inventory planning
                """;
            default -> """
                - Follow standard product creation guidelines
                - Ensure category-appropriate specifications
                - Consider market positioning and competition
                - Maintain consistency with similar products
                """;
        };
    }

    private String getComplexityGuidelines(String complexity) {
        return switch (complexity) {
            case "complex" -> """
                Complex products require additional attention to:
                - Detailed technical specifications and compatibility
                - Comprehensive installation or usage instructions
                - Extended warranty and support considerations
                - Higher-touch customer service requirements
                - More thorough quality assurance processes
                """;
            default -> """
                Simple products focus on:
                - Clear, concise product descriptions
                - Straightforward pricing and availability
                - Standard warranty and return policies
                - Streamlined fulfillment processes
                - Basic customer support requirements
                """;
        };
    }

    private String getPriorityActions(String priority, String scope) {
        return switch (priority) {
            case "high" -> "Focus on revenue-impacting products and customer-facing data first";
            case "low" -> "Address non-critical items during maintenance windows";
            default -> "Balance business impact with resource availability";
        };
    }

    private String getTimeframeConsiderations(String timeframe) {
        return switch (timeframe) {
            case "daily" -> "Focus on operational metrics and immediate performance indicators";
            case "weekly" -> "Analyze short-term trends and weekly performance patterns";
            case "monthly" -> "Review business performance and monthly trend analysis";
            case "quarterly" -> "Strategic analysis with seasonal and quarterly trend assessment";
            default -> "Balanced analysis appropriate for the specified timeframe";
        };
    }

    private String getScopeConsiderations(String scope) {
        return switch (scope) {
            case "category" -> "Category-specific considerations including business rules and validation";
            case "price-range" -> "Price-tier specific considerations including margin and competitive positioning";
            default -> "Comprehensive approach covering all products with appropriate prioritization";
        };
    }
}
