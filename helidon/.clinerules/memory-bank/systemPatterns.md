# System Patterns

## System Architecture
The product catalog service is designed as a microservice, adhering to a clean architecture pattern. It consists of distinct layers:
- **Resource Layer (API/Controllers)**: Handles incoming HTTP requests, validates input, and orchestrates calls to the service layer.
- **Service Layer (Business Logic)**: ✅ **IMPLEMENTED** - Contains the core business rules, SKU generation logic, domain validation, and orchestrates interactions with the repository layer.
- **Repository Layer (Data Access)**: ✅ **IMPLEMENTED** - Abstracts database interactions, providing methods for CRUD operations on products using Helidon DB Client.

## Key Technical Decisions
- **RESTful API**: Standard REST principles for resource exposure.
- **Dependency Injection**: ✅ **IMPLEMENTED** - Helidon's built-in CDI (Contexts and Dependency Injection) for managing component dependencies.
- **Configuration**: Externalized configuration using MicroProfile Config.
- **JSON Processing**: Jakarta JSON Binding (JSON-B) for object-to-JSON mapping.
- **Reactive Programming**: CompletableFuture-based async operations throughout service layer.
- **Domain Exceptions**: Custom exception classes for clean error handling (ProductNotFoundException, DuplicateSkuException).

## Design Patterns in Use
- **Repository Pattern**: ✅ **IMPLEMENTED** - To abstract data persistence logic.
- **Service Layer Pattern**: ✅ **IMPLEMENTED** - To encapsulate business logic with comprehensive validation.
- **Dependency Injection**: ✅ **IMPLEMENTED** - For loose coupling and testability using CDI @ApplicationScoped and @Inject.
- **Exception Handling Pattern**: Custom domain exceptions for specific error scenarios.

## Component Relationships
- `ProductResource` (Resource Layer) depends on `ProductService`.
- `ProductService` (Service Layer) ✅ **IMPLEMENTED** - depends on `ProductRepository`.
- `ProductRepository` (Repository Layer) ✅ **IMPLEMENTED** - interacts with the PostgreSQL database via Helidon DB Client.

## Critical Implementation Paths
- **Product Creation**: ✅ **IMPLEMENTED** - Involves input validation, SKU generation ([NNN]-###### format), database insertion, and transactional integrity for batch operations.
- **Product Retrieval**: ✅ **IMPLEMENTED** - Involves querying the database by ID/SKU/category or fetching paginated lists with proper not-found handling.
- **Product Update/Deletion**: ✅ **IMPLEMENTED** - Involves identifying the product, validating SKU uniqueness, and performing the respective database operation.

## SKU Generation Logic
- **Format**: `[NNN]-######` where NNN is category prefix and ###### is 6-digit random number
- **Category Mapping**: Electronics→ELC, Clothing→CLO, Books→BOO, etc.
- **Fallback**: Random prefix selection when category is unknown or missing
- **Uniqueness**: Repository-level validation ensures no duplicate SKUs

## Last Updated
2025-08-10 by Claude (Model: claude-sonnet-4, Task: Completed task-9 ProductService implementation)
