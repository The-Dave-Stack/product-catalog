/**
 * ProductNotFoundException.java
 *
 * <p>Purpose: - Custom exception for product-related not found scenarios - Provides specific error
 * handling for missing products in business operations
 *
 * <p>Last Updated: 2025-08-10 by Claude (Model: claude-sonnet-4, Task: task-9 - Create custom
 * exception classes)
 */
package com.thedavestack.productcatalog.service;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
