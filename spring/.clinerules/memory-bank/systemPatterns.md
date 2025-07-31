# System Patterns: Architecture and Design

## High-Level Architecture
The application follows a classic three-layer architecture, ensuring a clean separation of concerns:

1.  **Presentation Layer (`ProductController`)**: Handles incoming HTTP requests, validates data using DTOs (Data Transfer Objects), and delegates to the service layer. It is responsible for the API contract.
2.  **Service Layer (`ProductService`)**: Contains the core business logic. It orchestrates data operations, enforces business rules (like SKU generation), and manages transactions.
3.  **Data Access Layer (`ProductRepository`)**: Manages all interactions with the PostgreSQL database using Spring Data JPA. It abstracts the underlying data source from the rest of the application.

## Key Technical Decisions
- **Database Schema Management**: The database schema is managed exclusively through versioned SQL migration scripts using **Flyway**. This ensures that the schema is always in a predictable and consistent state across all environments. Hibernate's `ddl-auto` is set to `validate` to prevent accidental schema modifications by the application.
- **Entity-DTO Separation**: The API uses a strict separation between JPA entities (`Product`) and DTOs (`ProductResponse`, `CreateProductRequest`). This prevents exposing internal data structures and allows the API contract to evolve independently of the database schema.
- **Transactional Integrity**: All operations that modify the database, especially the batch creation of products, are executed within a single, atomic transaction (`@Transactional`). This guarantees that the system remains in a consistent state, even if errors occur mid-operation.
- **Containerized Environment**: The entire application stack, including the PostgreSQL database, is designed to be run within Docker containers managed by **Docker Compose**. This provides a consistent, isolated, and easily reproducible environment for both development and testing.
- **Comprehensive Testing Strategy**:
    - **Unit Tests**: The service layer is tested in isolation using Mockito to mock repository dependencies.
    - **Repository Tests**: The data access layer is tested using `@DataJpaTest`, which provides an in-memory database to verify JPA queries.
    - **Integration & E2E Tests**: The full application is tested using **Testcontainers**, which spins up the entire `docker-compose` stack (including the real PostgreSQL database) to validate the interactions between all layers, from the API endpoints down to the database.
- **Automated Code Formatting**: Code quality and consistency are enforced automatically by the **Spotless Maven Plugin**. It is configured to use `google-java-format` and enforce a standard import order, ensuring all code adheres to the project's style guide without manual intervention.
