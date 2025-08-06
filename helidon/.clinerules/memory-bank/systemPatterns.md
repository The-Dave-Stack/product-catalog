# System Patterns

## System Architecture
The product catalog service is designed as a microservice, adhering to a clean architecture pattern. It will consist of distinct layers:
- **Resource Layer (API/Controllers)**: Handles incoming HTTP requests, validates input, and orchestrates calls to the service layer.
- **Service Layer (Business Logic)**: Contains the core business rules and orchestrates interactions with the repository layer.
- **Repository Layer (Data Access)**: Abstracts database interactions, providing methods for CRUD operations on products.

## Key Technical Decisions
- **RESTful API**: Standard REST principles for resource exposure.
- **Dependency Injection**: Helidon's built-in CDI (Contexts and Dependency Injection) for managing component dependencies.
- **Configuration**: Externalized configuration using MicroProfile Config.
- **JSON Processing**: Jakarta JSON Binding (JSON-B) for object-to-JSON mapping.

## Design Patterns in Use
- **Repository Pattern**: To abstract data persistence logic.
- **Service Layer Pattern**: To encapsulate business logic.
- **Dependency Injection**: For loose coupling and testability.

## Component Relationships
- `ProductResource` (Resource Layer) depends on `ProductService`.
- `ProductService` (Service Layer) depends on `ProductRepository`.
- `ProductRepository` (Repository Layer) interacts with the PostgreSQL database via Helidon DB Client.

## Critical Implementation Paths
- **Product Creation**: Involves input validation, SKU generation (if needed), database insertion, and transactional integrity for batch operations.
- **Product Retrieval**: Involves querying the database by ID or fetching paginated lists.
- **Product Update/Deletion**: Involves identifying the product and performing the respective database operation.

## Last Updated
2025-08-06 by Cline (Model: claude-3-opus, Task: Initialized memory bank)
