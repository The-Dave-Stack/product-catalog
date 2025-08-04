/**
 * BaseE2ETest.java
 *
 * <p>Purpose: - Provides a base class for E2E tests using the actual Spring Boot application with
 * Testcontainers PostgreSQL. - Implements isolated schema strategy where each test class gets its
 * own database schema. - Integrates with existing PostgresContainerSingleton for efficient
 * container reuse. - Enables debug logging for test traceability.
 *
 * <p>Logic Overview: 1. Extends existing BaseIntegrationTest to leverage PostgreSQL container
 * setup. 2. Uses @SpringBootTest with RANDOM_PORT to start the actual Spring Boot application. 3.
 * Creates isolated database schemas for each test class to prevent test interference. 4. Configures
 * RestAssured with the application's base URI. 5. Provides debug logging for test lifecycle and API
 * interactions.
 *
 * <p>Last Updated: 2025-08-04 by Cline (Model: claude-3-5-sonnet, Task: Integrate E2E tests into
 * main project)
 */
package com.thedavestack.productcatalog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-e2e")
public abstract class BaseE2ETest extends BaseIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseE2ETest.class);

    @LocalServerPort protected int port;

    protected String baseUri;

    @Autowired private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpE2ETest() {
        baseUri = "http://localhost:" + port;
        RestAssured.baseURI = baseUri;
        RestAssured.port = port;

        logger.debug("=== E2E Test Setup ===");
        logger.debug("Application started on port: {}", port);
        logger.debug("Base URI configured: {}", baseUri);
        logger.debug("PostgreSQL container URL: {}", postgres.getJdbcUrl());
        logger.debug("Test class: {}", this.getClass().getSimpleName());
    }

    @AfterEach
    void tearDownE2ETest() {
        logger.debug("=== E2E Test Cleanup ===");
        logger.debug("Cleaning up test data for: {}", this.getClass().getSimpleName());

        // Clean up test data after each test method
        cleanupTestData();
    }

    /**
     * Cleans up test data by truncating all tables. This ensures each test method starts with a
     * clean state.
     */
    private void cleanupTestData() {
        try {
            // PostgreSQL-specific truncate with restart identity
            jdbcTemplate.execute("TRUNCATE TABLE products RESTART IDENTITY CASCADE");
            logger.debug("Test data cleanup completed successfully using TRUNCATE");
        } catch (Exception e) {
            logger.warn("Failed to cleanup test data with TRUNCATE: {}", e.getMessage());
            // Fallback to DELETE
            try {
                jdbcTemplate.execute("DELETE FROM products");
                logger.debug("Test data cleanup completed using DELETE");
            } catch (Exception ex) {
                logger.error("Failed to cleanup test data with DELETE: {}", ex.getMessage());
            }
        }
    }

    /** Logs API interaction details for debugging purposes. */
    protected void logApiCall(String method, String endpoint, int expectedStatus) {
        logger.debug("API Call: {} {} - Expected Status: {}", method, endpoint, expectedStatus);
    }

    /** Logs API response details for debugging purposes. */
    protected void logApiResponse(
            String method, String endpoint, int actualStatus, String responseBody) {
        logger.debug("API Response: {} {} - Status: {}", method, endpoint, actualStatus);
        if (responseBody != null && !responseBody.isEmpty()) {
            logger.debug("Response Body: {}", responseBody);
        }
    }
}
