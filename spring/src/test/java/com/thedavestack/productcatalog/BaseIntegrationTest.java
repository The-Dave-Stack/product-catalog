/**
 * BaseIntegrationTest.java
 *
 * <p>Purpose: - Provides a base configuration for integration tests that require a running
 * PostgreSQL database.
 *
 * <p>Logic Overview: - Uses Testcontainers to start a PostgreSQL container before any tests in a
 * subclass run. - The container is static to ensure it's started only once for all tests in the
 * suite, improving performance. - A @DynamicPropertySource method dynamically configures the Spring
 * Boot datasource properties to connect to the Testcontainer instance.
 *
 * <p>Last Updated: 2025-07-31 by Cline (Model: claude-3-opus, Task: Refactor test configuration)
 */
package com.thedavestack.productcatalog;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BaseIntegrationTest {

    static final PostgreSQLContainer<?> postgres = PostgresContainerSingleton.getInstance();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}
