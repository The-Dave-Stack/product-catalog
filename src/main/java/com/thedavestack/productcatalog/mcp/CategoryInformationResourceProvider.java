package com.thedavestack.productcatalog.mcp;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
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
 * MCP Resource Provider for Category Information. Provides detailed information about product
 * categories, their descriptions, business rules, and management guidelines for AI interactions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryInformationResourceProvider {

    private final ObjectMapper objectMapper;

    /**
     * Provides MCP resource specifications for category information.
     *
     * @return List of synchronous resource specifications
     */
    public List<McpServerFeatures.SyncResourceSpecification> getResourceSpecifications() {
        return List.of(
                // All categories overview resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "categories://overview/all",
                                "All Categories Overview",
                                "Complete overview of all product categories with descriptions and guidelines",
                                "application/json",
                                null),
                        this::getAllCategoriesResource),

                // Specific category details resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "categories://details/{category}",
                                "Category Details",
                                "Detailed information about a specific category including business rules and best practices",
                                "application/json",
                                null),
                        this::getCategoryDetailsResource),

                // Category management guidelines resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "categories://guidelines/management",
                                "Category Management Guidelines",
                                "Best practices and guidelines for managing products across different categories",
                                "application/json",
                                null),
                        this::getCategoryManagementGuidelinesResource),

                // Category validation rules resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "categories://rules/validation",
                                "Category Validation Rules",
                                "Validation rules and constraints for each product category",
                                "application/json",
                                null),
                        this::getCategoryValidationRulesResource));
    }

    private McpSchema.ReadResourceResult getAllCategoriesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            List<Map<String, Object>> categoriesInfo =
                    Arrays.stream(Category.values())
                            .map(this::createCategoryOverview)
                            .collect(Collectors.toList());

            String jsonContent =
                    objectMapper.writeValueAsString(
                            Map.of(
                                    "totalCategories",
                                    Category.values().length,
                                    "categories",
                                    categoriesInfo,
                                    "description",
                                    "Complete catalog of product categories with their descriptions and key characteristics",
                                    "lastUpdated",
                                    Instant.now().toString()));

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get all categories resource", e);
            throw new RuntimeException("Failed to retrieve categories overview", e);
        }
    }

    private McpSchema.ReadResourceResult getCategoryDetailsResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            String uriPath = request.uri().toString();
            String categoryName = extractPathParameter(uriPath, "category");

            if (categoryName == null) {
                throw new IllegalArgumentException("Category parameter is required");
            }

            Category category = Category.valueOf(categoryName.toUpperCase());
            Map<String, Object> categoryDetails = createDetailedCategoryInfo(category);

            String jsonContent = objectMapper.writeValueAsString(categoryDetails);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get category details resource", e);
            throw new RuntimeException("Failed to retrieve category details", e);
        }
    }

    private McpSchema.ReadResourceResult getCategoryManagementGuidelinesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> guidelines =
                    Map.of(
                            "generalPrinciples",
                                    List.of(
                                            "Ensure products are assigned to the most specific applicable category",
                                            "Review category assignments regularly for accuracy",
                                            "Consider seasonal variations in category performance",
                                            "Maintain consistent naming conventions within categories"),
                            "categorySpecificGuidelines",
                                    Arrays.stream(Category.values())
                                            .collect(
                                                    Collectors.toMap(
                                                            Category::name,
                                                            this::getCategorySpecificGuidelines)),
                            "inventoryManagement",
                                    Map.of(
                                            "stockLevels",
                                                    "Adjust minimum stock levels based on category seasonality",
                                            "reorderPoints",
                                                    "Electronics and Books require faster reorder cycles",
                                            "pricing",
                                                    "Consider category-specific price elasticity",
                                            "promotions",
                                                    "Coordinate promotions within related categories"),
                            "bestPractices",
                                    List.of(
                                            "Use bulk operations for category-wide updates",
                                            "Monitor category performance metrics regularly",
                                            "Implement category-specific quality standards",
                                            "Train staff on category-specific product knowledge"),
                            "commonMistakes",
                                    List.of(
                                            "Assigning products to overly broad categories",
                                            "Ignoring seasonal category trends",
                                            "Inconsistent pricing within categories",
                                            "Poor cross-category inventory balance"));

            String jsonContent = objectMapper.writeValueAsString(guidelines);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get category management guidelines resource", e);
            throw new RuntimeException("Failed to retrieve category management guidelines", e);
        }
    }

    private McpSchema.ReadResourceResult getCategoryValidationRulesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> validationRules =
                    Map.of(
                            "globalRules",
                                    Map.of(
                                            "requiredFields",
                                                    List.of(
                                                            "name",
                                                            "description",
                                                            "price",
                                                            "category",
                                                            "stockQuantity"),
                                            "nameLength",
                                                    "Minimum 3 characters, maximum 100 characters",
                                            "descriptionLength",
                                                    "Minimum 10 characters, maximum 500 characters",
                                            "priceRange",
                                                    "Must be greater than 0, maximum 99,999.99",
                                            "stockQuantity", "Must be non-negative integer"),
                            "categorySpecificRules",
                                    Arrays.stream(Category.values())
                                            .collect(
                                                    Collectors.toMap(
                                                            Category::name,
                                                            this::getCategoryValidationRules)),
                            "businessRules",
                                    Map.of(
                                            "skuGeneration",
                                                    "SKU format varies by category for optimal organization",
                                            "pricing",
                                                    "Electronics require competitive pricing analysis",
                                            "inventory",
                                                    "Books have different storage requirements than Electronics",
                                            "shipping",
                                                    "Automotive parts may have shipping restrictions"),
                            "dataQuality",
                                    Map.of(
                                            "requiredImages",
                                                    "All products should have at least one product image",
                                            "dimensions",
                                                    "Physical products must include weight and dimensions",
                                            "specifications",
                                                    "Technical products require detailed specifications",
                                            "compatibility",
                                                    "Automotive parts require compatibility information"));

            String jsonContent = objectMapper.writeValueAsString(validationRules);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get category validation rules resource", e);
            throw new RuntimeException("Failed to retrieve category validation rules", e);
        }
    }

    private Map<String, Object> createCategoryOverview(Category category) {
        return Map.of(
                "name", category.name(),
                "displayName", formatCategoryDisplayName(category),
                "description", getCategoryDescription(category),
                "keyCharacteristics", getCategoryCharacteristics(category),
                "typicalPriceRange", getTypicalPriceRange(category),
                "inventoryConsiderations", getInventoryConsiderations(category));
    }

    private Map<String, Object> createDetailedCategoryInfo(Category category) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.putAll(
                Map.of(
                        "category", category.name(),
                        "detailedDescription", getDetailedCategoryDescription(category),
                        "characteristics", getCategoryCharacteristics(category),
                        "examples", getCategoryExamples(category),
                        "pricingGuidelines", getPricingGuidelines(category),
                        "inventoryGuidelines", getInventoryGuidelines(category),
                        "seasonality", getSeasonalityInfo(category),
                        "competitorAnalysis", getCompetitorAnalysis(category),
                        "marketTrends", getMarketTrends(category),
                        "managementTips", getManagementTips(category)));
        resultMap.putAll(
                Map.of(
                        "displayName", formatCategoryDisplayName(category),
                        "description", getCategoryDescription(category)));
        return resultMap;
    }

    private String formatCategoryDisplayName(Category category) {
        return switch (category) {
            case ELECTRONICS -> Category.ELECTRONICS.getDisplayName();
            case BOOKS -> Category.BOOKS.getDisplayName();
            case CLOTHING -> Category.CLOTHING.getDisplayName();
            case HOME_GARDEN -> Category.HOME_GARDEN.getDisplayName();
            case SPORTS_OUTDOORS -> Category.SPORTS_OUTDOORS.getDisplayName();
            case TOYS_GAMES -> Category.TOYS_GAMES.getDisplayName();
            case HEALTH_BEAUTY -> Category.HEALTH_BEAUTY.getDisplayName();
            case FOOD_BEVERAGES -> Category.FOOD_BEVERAGES.getDisplayName();
            case AUTOMOTIVE -> Category.AUTOMOTIVE.getDisplayName();
            case NOT_CATEGORIZED -> Category.NOT_CATEGORIZED.getDisplayName();
        };
    }

    private String getCategoryDescription(Category category) {
        return switch (category) {
            case ELECTRONICS -> "Electronic devices, gadgets, and technology products";
            case BOOKS -> "Physical and digital books across all genres and subjects";
            case CLOTHING -> "Apparel, accessories, and fashion items for all demographics";
            case HOME_GARDEN -> "Home improvement, furniture, and gardening products";
            case SPORTS_OUTDOORS -> "Sports equipment, fitness gear, and recreational activities";
            case TOYS_GAMES -> "Children's toys, games, and educational products";
            case HEALTH_BEAUTY -> "Personal care, cosmetics, and health products";
            case FOOD_BEVERAGES -> "Food and beverages";
            case AUTOMOTIVE -> "Vehicle parts, accessories, and maintenance products";
            case NOT_CATEGORIZED -> "Product not categorized";
        };
    }

    private String getDetailedCategoryDescription(Category category) {
        return switch (category) {
            case ELECTRONICS -> "Comprehensive range of consumer electronics including smartphones, laptops, tablets, smart home devices, audio equipment, and gaming systems. Focus on latest technology trends and competitive pricing.";
            case BOOKS -> "Extensive collection of books covering fiction, non-fiction, educational, professional, and specialty titles. Includes both physical books and digital editions with rapid inventory turnover.";
            case CLOTHING -> "Fashion-forward clothing and accessories for men, women, and children. Seasonal collections with emphasis on style, comfort, and affordability across various price points.";
            case HOME_GARDEN -> "Complete home improvement solutions including furniture, decor, tools, appliances, and gardening supplies. Focus on quality and functionality for modern living spaces.";
            case SPORTS_OUTDOORS -> "Professional and recreational sports equipment for all activity levels. Includes fitness equipment, outdoor gear, team sports, and individual training products.";
            case TOYS_GAMES -> "Educational and entertaining toys for children of all ages. Emphasis on safety, development, and fun with both traditional and modern interactive options.";
            case HEALTH_BEAUTY -> "Personal wellness and beauty products including skincare, cosmetics, supplements, and health monitoring devices. Focus on quality ingredients and proven effectiveness.";
            case FOOD_BEVERAGES -> "Diverse range of food and beverage products including snacks, beverages, and specialty items. Focus on quality ingredients and consumer preferences.";
            case AUTOMOTIVE -> "Comprehensive automotive solutions including replacement parts, accessories, maintenance supplies, and performance upgrades for various vehicle types and models.";
            case NOT_CATEGORIZED -> "Products that do not fit into any specific category. This includes unique or specialized items that require individual handling.";
        };
    }

    private List<String> getCategoryCharacteristics(Category category) {
        return switch (category) {
            case ELECTRONICS -> List.of(
                    "High-tech features",
                    "Rapid product evolution",
                    "Warranty considerations",
                    "Price sensitivity");
            case BOOKS -> List.of(
                    "Educational value", "Genre diversity", "Author reputation", "Format options");
            case CLOTHING -> List.of(
                    "Size variations", "Seasonal trends", "Material quality", "Style preferences");
            case HOME_GARDEN -> List.of(
                    "Functionality focus",
                    "Quality materials",
                    "Space optimization",
                    "Seasonal demand");
            case SPORTS_OUTDOORS -> List.of(
                    "Performance oriented",
                    "Safety standards",
                    "Activity specific",
                    "Durability focus");
            case TOYS_GAMES -> List.of(
                    "Age appropriate",
                    "Safety certified",
                    "Educational value",
                    "Entertainment factor");
            case HEALTH_BEAUTY -> List.of(
                    "Ingredient focus",
                    "Personal preference",
                    "Skin type specific",
                    "Health benefits");
            case FOOD_BEVERAGES -> List.of(
                    "Quality ingredients",
                    "Shelf life considerations",
                    "Dietary restrictions",
                    "Flavor profiles");
            case AUTOMOTIVE -> List.of(
                    "Vehicle compatibility",
                    "Performance impact",
                    "Installation requirements",
                    "Regulatory compliance");
            case NOT_CATEGORIZED -> List.of(
                    "Unique characteristics",
                    "Special handling requirements",
                    "Individual product focus");
        };
    }

    private List<String> getCategoryExamples(Category category) {
        return switch (category) {
            case ELECTRONICS -> List.of(
                    "iPhone 15 Pro",
                    "MacBook Air M3",
                    "Samsung Smart TV",
                    "Sony Headphones",
                    "Nintendo Switch");
            case BOOKS -> List.of(
                    "Fiction novels",
                    "Technical manuals",
                    "Children's books",
                    "Self-help guides",
                    "Academic textbooks");
            case CLOTHING -> List.of(
                    "Designer jeans",
                    "Athletic wear",
                    "Formal suits",
                    "Casual t-shirts",
                    "Winter jackets");
            case HOME_GARDEN -> List.of(
                    "Kitchen appliances",
                    "Garden tools",
                    "Furniture sets",
                    "Home decor",
                    "Lighting fixtures");
            case SPORTS_OUTDOORS -> List.of(
                    "Tennis rackets",
                    "Yoga mats",
                    "Hiking boots",
                    "Fitness trackers",
                    "Golf clubs");
            case TOYS_GAMES -> List.of(
                    "LEGO sets",
                    "Board games",
                    "Action figures",
                    "Educational puzzles",
                    "Remote control cars");
            case HEALTH_BEAUTY -> List.of(
                    "Skincare serums",
                    "Makeup palettes",
                    "Vitamins",
                    "Fitness supplements",
                    "Hair care products");
            case FOOD_BEVERAGES -> List.of(
                    "Organic snacks",
                    "Gourmet coffee",
                    "Craft beers",
                    "Gluten-free pasta",
                    "Vegan protein bars");
            case AUTOMOTIVE -> List.of(
                    "Brake pads",
                    "Engine oil",
                    "Car accessories",
                    "Diagnostic tools",
                    "Performance parts");
            case NOT_CATEGORIZED -> List.of(
                    "Custom-made items",
                    "Limited edition collectibles",
                    "Specialty tools",
                    "Unique art pieces",
                    "Niche market products");
        };
    }

    private String getTypicalPriceRange(Category category) {
        return switch (category) {
            case ELECTRONICS -> "$10 - $3,000";
            case BOOKS -> "$5 - $200";
            case CLOTHING -> "$10 - $500";
            case HOME_GARDEN -> "$15 - $2,000";
            case SPORTS_OUTDOORS -> "$20 - $1,500";
            case TOYS_GAMES -> "$5 - $300";
            case HEALTH_BEAUTY -> "$8 - $250";
            case FOOD_BEVERAGES -> "$3 - $200";
            case AUTOMOTIVE -> "$10 - $1,000";
            case NOT_CATEGORIZED -> "$10 - $3,000";
        };
    }

    private String getInventoryConsiderations(Category category) {
        return switch (category) {
            case ELECTRONICS -> "Fast-moving, technology obsolescence risk";
            case BOOKS -> "Seasonal peaks, slow-moving backlist";
            case CLOTHING -> "Size distribution, seasonal turnover";
            case HOME_GARDEN -> "Seasonal peaks, storage space requirements";
            case SPORTS_OUTDOORS -> "Seasonal demand, equipment durability";
            case TOYS_GAMES -> "Holiday seasonality, safety compliance";
            case HEALTH_BEAUTY -> "Expiration dates, personal preferences";
            case FOOD_BEVERAGES -> "Perishable items, dietary trends";
            case AUTOMOTIVE -> "Vehicle compatibility, technical specifications";
            case NOT_CATEGORIZED -> "Unique handling requirements, individual product focus";
        };
    }

    private Map<String, String> getCategorySpecificGuidelines(Category category) {
        return switch (category) {
            case ELECTRONICS -> Map.of(
                    "pricing", "Monitor competitor prices daily due to market volatility",
                    "inventory", "Maintain 30-45 days of stock due to rapid product cycles",
                    "quality", "Ensure all electronics meet safety and performance standards");
            case BOOKS -> Map.of(
                    "pricing", "Consider educational vs entertainment pricing strategies",
                    "inventory", "Balance bestsellers with backlist titles",
                    "quality", "Verify ISBN accuracy and edition details");
            case CLOTHING -> Map.of(
                    "pricing", "Account for seasonal pricing fluctuations",
                    "inventory", "Maintain size distribution based on demographics",
                    "quality", "Ensure fabric quality and sizing accuracy");
            default -> Map.of(
                    "pricing", "Regular market analysis recommended",
                    "inventory", "Standard inventory management practices apply",
                    "quality", "Maintain category-specific quality standards");
        };
    }

    private Map<String, Object> getCategoryValidationRules(Category category) {
        return switch (category) {
            case ELECTRONICS -> Map.of(
                    "requiredFields", List.of("brand", "model", "specifications", "warranty"),
                    "priceValidation", "Must include competitive analysis",
                    "descriptionRules", "Must include technical specifications");
            case AUTOMOTIVE -> Map.of(
                    "requiredFields", List.of("compatibility", "partNumber", "brand"),
                    "specialValidation", "Vehicle compatibility verification required",
                    "safetyRequirements", "Must meet automotive safety standards");
            default -> Map.of(
                    "standardValidation", "Standard product validation rules apply",
                    "categorySpecific", "No additional validation rules");
        };
    }

    private Map<String, String> getPricingGuidelines(Category category) {
        return Map.of(
                "strategy", "Competitive pricing based on market analysis",
                "frequency", "Review prices weekly for high-turnover categories",
                "margins", "Maintain healthy margins while staying competitive");
    }

    private Map<String, String> getInventoryGuidelines(Category category) {
        return Map.of(
                "reorderPoint", "Set based on lead time and demand variability",
                "safetyStock", "Maintain 1-2 weeks of safety stock",
                "seasonality", "Adjust for seasonal demand patterns");
    }

    private Map<String, String> getSeasonalityInfo(Category category) {
        return switch (category) {
            case TOYS_GAMES -> Map.of("peak", "November-December", "low", "January-August");
            case CLOTHING -> Map.of("peak", "Spring/Fall transitions", "low", "Mid-season periods");
            case HOME_GARDEN -> Map.of("peak", "Spring-Summer", "low", "Winter");
            default -> Map.of("seasonality", "Moderate seasonal variation");
        };
    }

    private Map<String, String> getCompetitorAnalysis(Category category) {
        return Map.of(
                "keyCompetitors", "Amazon, Best Buy, Target, Walmart",
                "differentiators", "Customer service, product quality, pricing",
                "marketPosition", "Competitive in price and selection");
    }

    private List<String> getMarketTrends(Category category) {
        return switch (category) {
            case ELECTRONICS -> List.of(
                    "AI integration", "Sustainability focus", "5G connectivity");
            case HEALTH_BEAUTY -> List.of(
                    "Natural ingredients", "Personalization", "Wellness focus");
            default -> List.of("Digital transformation", "Sustainability", "Personalization");
        };
    }

    private List<String> getManagementTips(Category category) {
        return List.of(
                "Monitor category performance metrics regularly",
                "Stay updated on industry trends and consumer preferences",
                "Maintain optimal inventory levels based on demand patterns",
                "Focus on customer satisfaction and product quality");
    }

    private String extractPathParameter(String uriPath, String paramName) {
        // Extract parameter from URI path like categories://details/ELECTRONICS
        String[] pathParts = uriPath.split("/");
        if (pathParts.length > 0 && "category".equals(paramName)) {
            return pathParts[pathParts.length - 1]; // Last part of the path
        }
        return null;
    }
}
