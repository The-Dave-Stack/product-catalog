/**
 * ProductNotFoundException.java
 *
 * <p>Design Doc: ./docs/API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Custom exception to indicate that a requested product could not be found.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: Initial creation for task-9)
 */
package com.thedavestack.productcatalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long id) {
        super(String.format("Product not found with id: %d", id));
    }
}
