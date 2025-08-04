/**
 * BaseE2ETest.java
 *
 * Purpose:
 * - Provides a base class for E2E tests, managing PostgreSQL and Spring Boot containers using Testcontainers.
 * - Ensures the entire application stack is up and running before tests.
 *
 * Logic Overview:
 * 1. Uses PostgreSQLContainer to start a PostgreSQL database.
 * 2. Uses GenericContainer to start the Spring Boot application with the correct database connection.
 * 3. Waits for the Spring Boot application to be healthy before tests begin.
 * 4. Provides the base URI for the API for use in test methods.
 *
 * Last Updated:
 * 2025-08-04 by Cline (Model: claude-3-5-sonnet, Task: Simplified E2E test setup using individual containers)
 */
package com.thedavestack.productcatalog.e2e;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Testcontainers
public abstract class BaseE2ETest {

    private static final int APP_PORT = 8080;
    private static final String POSTGRES_DB_NAME = "product_catalog";
    private static final String POSTGRES_USERNAME = "user";
    private static final String POSTGRES_PASSWORD = "password";

    protected static String BASE_URI;

    private static final Network network = Network.newNetwork();

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.5-alpine"))
            .withDatabaseName(POSTGRES_DB_NAME)
            .withUsername(POSTGRES_USERNAME)
            .withPassword(POSTGRES_PASSWORD)
            .withNetwork(network)
            .withNetworkAliases("postgres-db");

    @Container
    public static GenericContainer<?> appContainer = new GenericContainer<>(DockerImageName.parse("spring-product-catalog-spring:latest"))
            .withExposedPorts(APP_PORT)
            .withNetwork(network)
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres-db:5432/" + POSTGRES_DB_NAME)
            .withEnv("SPRING_DATASOURCE_USERNAME", POSTGRES_USERNAME)
            .withEnv("SPRING_DATASOURCE_PASSWORD", POSTGRES_PASSWORD)
            .dependsOn(postgresContainer)
            .waitingFor(Wait.forHttp("/actuator/health")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(3)));

    @BeforeAll
    static void setup() {
        System.out.println("Starting PostgreSQL container...");
        postgresContainer.start();
        
        System.out.println("Starting Spring Boot application container...");
        appContainer.start();
        
        BASE_URI = String.format("http://%s:%d",
                appContainer.getHost(),
                appContainer.getMappedPort(APP_PORT));
        System.out.println("Application Base URI: " + BASE_URI);
        System.out.println("All containers started successfully!");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("Stopping containers...");
        if (appContainer != null) {
            appContainer.stop();
        }
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
        if (network != null) {
            network.close();
        }
    }
}
