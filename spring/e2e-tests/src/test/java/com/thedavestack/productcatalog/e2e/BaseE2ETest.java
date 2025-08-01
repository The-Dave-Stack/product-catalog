/**
 * BaseE2ETest.java
 *
 * Purpose:
 * - Provides a base class for E2E tests, managing the Docker Compose environment using Testcontainers.
 * - Ensures the entire application stack (Spring Boot app and PostgreSQL) is up and running before tests.
 *
 * Logic Overview:
 * 1. Uses DockerComposeContainer to start and stop the services defined in docker-compose.yml.
 * 2. Waits for the Spring Boot application to be healthy before tests begin.
 * 3. Provides the base URI for the API for use in test methods.
 *
 * Last Updated:
 * 2025-08-01 by Cline (Model: claude-3-opus, Task: Initial creation of BaseE2ETest)
 */
package com.thedavestack.productcatalog.e2e;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@Testcontainers
public abstract class BaseE2ETest {

    private static final String DOCKER_COMPOSE_FILE_PATH = "/home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring/docker-compose.yml";
    private static final String APP_SERVICE_NAME = "product-catalog-spring";
    private static final int APP_PORT = 8080;

    protected static String BASE_URI;

    @Container
    public static DockerComposeContainer<?> composeContainer =
            new DockerComposeContainer<>(new File(DOCKER_COMPOSE_FILE_PATH))
                    .withExposedService(APP_SERVICE_NAME, APP_PORT, Wait.forHttp("/actuator/health")
                            .forStatusCode(200)
                            .withStartupTimeout(Duration.ofSeconds(120)));

    @BeforeAll
    static void setup() {
        composeContainer.start();
        BASE_URI = String.format("http://%s:%d",
                composeContainer.getServiceHost(APP_SERVICE_NAME, APP_PORT),
                composeContainer.getServicePort(APP_SERVICE_NAME, APP_PORT));
        System.out.println("Application Base URI: " + BASE_URI);
    }

    @AfterAll
    static void cleanup() {
        composeContainer.stop();
    }
}
