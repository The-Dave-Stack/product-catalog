/**
 * PostgresContainerSingleton.java
 *
 * <p>Purpose: - Manages a single, shared instance of a PostgreSQL Testcontainer for the entire test
 * suite.
 *
 * <p>Logic Overview: - Implements the Singleton pattern to ensure only one instance of the
 * PostgreSQLContainer is created. - The container is started only once and reused across all tests,
 * significantly speeding up the test suite.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: Refactor test configuration)
 */
package com.thedavestack.productcatalog;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerSingleton {
    private static final PostgreSQLContainer<?> instance;

    static {
        instance = new PostgreSQLContainer<>("postgres:17.5-alpine");
        instance.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return instance;
    }
}
