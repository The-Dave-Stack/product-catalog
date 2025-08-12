# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running
- **Build project**: `mvn clean install`
- **Run application**: `mvn spring-boot:run`
- **Run with Docker**: `docker compose up` (recommended for development)

### Testing
- **Unit tests only**: `mvn test` (14 tests)
- **All tests**: `mvn verify` (28+ tests including integration and E2E)
- **Integration tests**: Available in `*IT.java` files with MockMvc + JWT
- **E2E tests**: Available in `*E2ETest.java` files with RestAssured + authentication

### Code Quality
- **Format code**: `mvn spotless:apply`
- **Check formatting**: `mvn spotless:check`

### Authentication Testing
- **Admin credentials**: `admin` / `admin123` (full CRUD access)
- **User credentials**: `user` / `user123` (read-only access)
- **Login endpoint**: `POST /api/v1/auth/login`
- **JWT token required**: All product endpoints require `Authorization: Bearer <token>`

## Architecture Overview

This is an **enterprise-grade Spring Boot 3.5.4 REST API** for product catalog management with comprehensive security, advanced features, and production-ready architecture:

### Package Structure
- **Controller Layer** (`controller/`): REST endpoints with JWT security (Products + Auth)
- **Service Layer** (`service/`): Business logic with transactions and audit logging
- **Repository Layer** (`repository/`): JPA data access with soft delete patterns
- **Security Layer** (`security/`): JWT authentication, filters, and authorization
- **DTO Layer** (`dto/`): Request/response objects with comprehensive validation
- **Validation Layer** (`validation/`): Custom validators and business rule enforcement
- **Exception Layer** (`exception/`): Global error handling with standardized responses
- **Config Layer** (`config/`): Security, async processing, and OpenAPI configuration

### Key Components - Enhanced
- **Product Entity**: Advanced model with inventory, categories, soft deletes, versioning
- **JWT Authentication**: Stateless auth with role-based access (ADMIN/USER)
- **Database**: PostgreSQL with 3 Flyway migrations and strategic indexing
- **Audit System**: Asynchronous audit logging for all CRUD operations
- **Testing Strategy**: 28+ tests (Unit, Integration, E2E) with 100% pass rate
- **API Documentation**: Enhanced Swagger UI with JWT security schemas
- **Soft Delete Pattern**: Data preservation using `@SQLDelete` and `@Where`

### Configuration - Production Ready
- **Profiles**: `default`, `test`, `test-e2e` with environment-specific configs
- **Security**: JWT configuration with configurable expiration and secret keys
- **Database**: Optimized connection pooling with HikariCP
- **Actuator**: Health checks and metrics for production monitoring
- **Async Processing**: Background audit logging and change tracking
- **Enhanced 404 Handling**: `spring.mvc.throw-exception-if-no-handler-found=true` for comprehensive error coverage

## Development Notes - Enterprise Features

### Database Schema - Enhanced
- **Flyway Migrations**: 3 production-ready migrations with automatic execution
- **Products Table**: Enhanced with inventory, categories, audit fields, soft delete
- **Strategic Indexing**: Indexes on `sku`, `category`, `stock_quantity`, `deleted`
- **Data Types**: UUID primary keys, BigDecimal for prices, timestamps for auditing
- **Soft Delete**: Logical deletion preserving data integrity and audit trails

### Authentication & Security
- **JWT Implementation**: Stateless authentication with configurable expiration
- **Role-Based Access**: `ADMIN` (full CRUD) vs `USER` (read-only) permissions
- **Security Filters**: JWT validation on all protected endpoints
- **Hardcoded Users**: Demo credentials for development and testing
- **Password Security**: Production-ready for external authentication integration

### Testing Patterns - Comprehensive
- **Unit Tests**: `@ExtendWith(MockitoExtension.class)` with service layer isolation
- **Integration Tests**: `@SpringBootTest` with MockMvc and JWT authentication  
- **E2E Tests**: Full Spring context with RestAssured and real HTTP requests
- **Database Tests**: Testcontainers with PostgreSQL for repository layer
- **Authentication Flow**: Complete JWT login and API access testing

### Error Handling - Enhanced with Developer Guidance
- **Custom Exceptions**: `ProductNotFoundException`, `DuplicateSkuException`
- **Enhanced Global Handler**: Consistent error responses with developer guidance
- **Developer-Friendly 404s**: All not-found errors include automatic Swagger UI links
- **Comprehensive Coverage**: Handles both `NoHandlerFoundException` and `NoResourceFoundException`
- **Help Links**: Automatic links to `/swagger-ui/index.html` and `/v3/api-docs` for guidance
- **Validation Errors**: Detailed field-level validation messages
- **Security Errors**: Proper 401/403 responses without sensitive information
- **Error Codes**: Standardized error codes for client integration

### API Features - Advanced
- **Pagination**: Configurable page size and sorting with Spring Data
- **Filtering**: Multi-criteria filtering (category, price range, name search)
- **Low Stock Alerts**: Dedicated endpoint for inventory management
- **Audit Logging**: Complete CRUD operation tracking with user context
- **Response DTOs**: Consistent API responses with proper HTTP status codes

### Performance Optimizations
- **Connection Pooling**: HikariCP with optimized database connections
- **Lazy Loading**: JPA relationships loaded on demand for performance
- **Async Processing**: Background audit logging to avoid request blocking
- **Database Indexes**: Strategic indexing for common query patterns
- **Soft Delete Queries**: Optimized `@Where` clauses for active data filtering

### Production Considerations
- **Environment Profiles**: Separate configurations for development, test, production
- **Docker Ready**: Complete containerization with health checks
- **Monitoring**: Actuator endpoints for application health and metrics
- **Security Headers**: CORS and security headers configured
- **Logging**: Structured logging ready for centralized log aggregation

## Enhanced Error Response Format

All error responses follow a standardized format with optional developer guidance:

```json
{
  "timestamp": "2025-08-12T10:31:36.844264303Z",
  "status": 404,
  "error": "Not Found", 
  "message": "No endpoint found for the requested path 'api/v1/invalid-endpoint'. Please check the URL and HTTP method.",
  "path": "/api/v1/invalid-endpoint",
  "errorCode": "ENDPOINT_NOT_FOUND",
  "helpLinks": [
    {
      "description": "API Documentation",
      "url": "/swagger-ui/index.html"
    },
    {
      "description": "OpenAPI Specification", 
      "url": "/v3/api-docs"
    }
  ]
}
```

### Error Types with Help Links
- **Product Not Found**: When requesting non-existent product IDs
- **Invalid Endpoints**: When accessing non-existent API endpoints
- **Missing Static Resources**: When Spring Boot treats invalid endpoints as static resources

This enhances developer experience by automatically providing navigation to API documentation when errors occur.