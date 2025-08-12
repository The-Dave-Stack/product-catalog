/**
 * DuplicateSkuException.java
 *
 * <p>Purpose: - Custom exception thrown when attempting to create a product with a SKU that already
 * exists.
 *
 * <p>Last Updated: 2025-08-04 by Cline (Model: claude-3-5-sonnet, Task: task-14)
 */
package com.thedavestack.productcatalog.exception;

public class DuplicateSkuException extends RuntimeException {
    public DuplicateSkuException(String sku) {
        super("Product with SKU " + sku + " already exists");
    }
}
