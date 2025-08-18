package com.thedavestack.productcatalog.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    BOOKS("Books"),
    HOME_GARDEN("Home & Garden"),
    SPORTS_OUTDOORS("Sports & Outdoors"),
    TOYS_GAMES("Toys & Games"),
    HEALTH_BEAUTY("Health & Beauty"),
    FOOD_BEVERAGES("Food & Beverages"),
    AUTOMOTIVE("Automotive"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static Category fromDisplayName(String displayName) {
        for (Category category : Category.values()) {
            if (category.displayName.equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + displayName);
    }

    /**
     * Custom deserializer that handles both enum names (for MCP compatibility) 
     * and display names (for REST API compatibility).
     * 
     * @param value the string value to convert to Category
     * @return the corresponding Category enum
     * @throws IllegalArgumentException if the value doesn't match any category
     */
    @JsonCreator
    public static Category fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Category value cannot be null or empty");
        }
        
        // First try enum name (MCP compatibility - handles "ELECTRONICS", "HOME_GARDEN", etc.)
        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fall back to display name (REST API compatibility - handles "Electronics", "Home & Garden", etc.)
            return fromDisplayName(value);
        }
    }
}
