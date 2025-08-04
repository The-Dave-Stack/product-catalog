# Agent Guidelines for Product Catalog Service (Spring Boot)

This document outlines the conventions and commands for agents operating within this repository.

## Build, Lint, and Test Commands

*   **Build Project**: `mvn clean install`
*   **Run All Tests**: `mvn test`
*   **Run a Single Test Class**: `mvn test -Dtest=YourTestClassName` (e.g., `mvn test -Dtest=ProductControllerTest`)
*   **Apply Code Formatting**: `mvn spotless:apply`
*   **Check Code Formatting**: `mvn spotless:check`

## Code Style Guidelines (Java)

This project adheres to the Google Java Format (AOSP style) enforced by Spotless.

*   **Formatting**: AOSP style, enforced by `spotless-maven-plugin`.
*   **Imports**: Ordered as `java`, `javax`, `org`, `com`. Unused imports are removed automatically.
*   **Naming Conventions**: Standard Java conventions (camelCase for variables/methods, PascalCase for classes, SCREAMING_SNAKE_CASE for constants).
*   **Error Handling**: Prefer specific exceptions over generic ones. Use custom exceptions for business logic errors (e.g., `ProductNotFoundException`).
*   **Dependency Injection**: Use Spring's `@Autowired` or constructor injection.
*   **Immutability**: Prefer immutable objects where possible, especially for DTOs.
*   **Logging**: Use SLF4J for logging.
