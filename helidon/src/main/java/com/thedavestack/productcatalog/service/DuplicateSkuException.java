/**
 * DuplicateSkuException.java
 *
 * <p>Purpose: - Custom exception for SKU duplication scenarios - Provides specific error handling
 * for SKU uniqueness violations in business operations
 *
 * <p>Last Updated: 2025-08-10 by Claude (Model: claude-sonnet-4, Task: task-9 - Create custom
 * exception classes)
 */
package com.thedavestack.productcatalog.service;

public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String message) {
        super(message);
    }

    public DuplicateSkuException(String message, Throwable cause) {
        super(message, cause);
    }
}
