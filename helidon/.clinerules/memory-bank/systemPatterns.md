# System Patterns

## System Architecture
The product catalog service is designed as a microservice, adhering to a clean architecture pattern. It consists of distinct layers:
- **Resource Layer (API/Controllers)**: ✅ **IMPLEMENTED** - Handles incoming HTTP requests, validates input using Jakarta Bean Validation, manages DTOs for request/response, and orchestrates calls to the service layer via JAX-RS endpoints.
- **Service Layer (Business Logic)**: ✅ **IMPLEMENTED** - Contains the core business rules, SKU generation logic, domain validation, and orchestrates interactions with the repository layer.
- **Repository Layer (Data Access)**: ✅ **IMPLEMENTED** - Abstracts database interactions, providing methods for CRUD operations on products using Helidon DB Client.

## Key Technical Decisions
- **RESTful API**: ✅ **IMPLEMENTED** - Standard REST principles with JAX-RS annotations and proper HTTP status codes.
- **Dependency Injection**: ✅ **IMPLEMENTED** - Helidon's built-in CDI (Contexts and Dependency Injection) for managing component dependencies.
- **Configuration**: Externalized configuration using MicroProfile Config.
- **JSON Processing**: ✅ **IMPLEMENTED** - Jakarta JSON Binding (JSON-B) for object-to-JSON mapping with DTOs.
- **Reactive Programming**: ✅ **IMPLEMENTED** - CompletableFuture-based async operations throughout all layers.
- **Domain Exceptions**: ✅ **IMPLEMENTED** - Custom exception classes with dedicated JAX-RS exception mappers.
- **Input Validation**: ✅ **IMPLEMENTED** - Jakarta Bean Validation with custom messages and structured error responses.
- **Error Handling**: ✅ **IMPLEMENTED** - Comprehensive exception mappers for all error scenarios with proper HTTP status codes.

## Design Patterns in Use
- **Repository Pattern**: ✅ **IMPLEMENTED** - To abstract data persistence logic.
- **Service Layer Pattern**: ✅ **IMPLEMENTED** - To encapsulate business logic with comprehensive validation.
- **Dependency Injection**: ✅ **IMPLEMENTED** - For loose coupling and testability using CDI @ApplicationScoped and @Inject.
- **Exception Handling Pattern**: ✅ **IMPLEMENTED** - Custom domain exceptions with dedicated JAX-RS exception mappers.
- **DTO Pattern**: ✅ **IMPLEMENTED** - Separate request/response DTOs for clean API contracts and validation boundaries.
- **Exception Mapper Pattern**: ✅ **IMPLEMENTED** - Centralized error handling with structured responses and proper HTTP status codes.

## Component Relationships
- `ProductResource` (Resource Layer) ✅ **IMPLEMENTED** - depends on `ProductService`.
- `ProductService` (Service Layer) ✅ **IMPLEMENTED** - depends on `ProductRepository`.
- `ProductRepository` (Repository Layer) ✅ **IMPLEMENTED** - interacts with the PostgreSQL database via Helidon DB Client.
- Exception Mappers ✅ **IMPLEMENTED** - handle all error scenarios and provide structured JSON responses.

## Critical Implementation Paths
- **Product Creation**: ✅ **IMPLEMENTED** - REST endpoint validation → DTO conversion → Service validation → SKU generation → Repository insertion → Response DTO.
- **Product Retrieval**: ✅ **IMPLEMENTED** - REST endpoint → Service orchestration → Repository query → Response DTO conversion → JSON response.
- **Product Update/Deletion**: ✅ **IMPLEMENTED** - REST validation → Service validation → Repository operation → Success/error response.
- **Error Handling**: ✅ **IMPLEMENTED** - Exception thrown → Exception mapper → Structured ErrorResponse → Proper HTTP status.

## SKU Generation Logic
- **Format**: `[NNN]-######` where NNN is category prefix and ###### is 6-digit random number
- **Category Mapping**: Electronics→ELC, Clothing→CLO, Books→BOO, etc.
- **Fallback**: Random prefix selection when category is unknown or missing
- **Uniqueness**: Repository-level validation ensures no duplicate SKUs

## REST API Implementation Details
- **Base Path**: `/api/v1/products` with JAX-RS @Path annotation
- **HTTP Methods**: Full CRUD support (POST, GET, PUT, DELETE) plus batch operations
- **Content Type**: `application/json` for all requests and responses
- **Async Processing**: All endpoints return CompletionStage for non-blocking operations
- **Validation**: Jakarta Bean Validation with custom error messages
- **Exception Handling**: 6 dedicated exception mappers for comprehensive error coverage:
  - ProductNotFoundExceptionMapper (404 Not Found)
  - DuplicateSkuExceptionMapper (409 Conflict)
  - IllegalArgumentExceptionMapper (400 Bad Request)
  - ValidationExceptionMapper (400 Bad Request with validation details)
  - GenericExceptionMapper (500 Internal Server Error)
  - ErrorResponse structure with timestamp, status, error, message, and path

## DTO Architecture
- **ProductResponse**: Complete product representation for GET responses
- **CreateProductRequest**: Input validation for product creation with required fields
- **BatchCreateProductRequest**: Wrapper for multiple product creation
- **ProductListResponse**: Paginated response with metadata (total, page, size, hasNext, hasPrevious)
- **ErrorResponse**: Standardized error response structure

## Last Updated
2025-08-11 by Claude (Model: claude-sonnet-4, Task: Completed task-10 ProductResource REST endpoints implementation)
