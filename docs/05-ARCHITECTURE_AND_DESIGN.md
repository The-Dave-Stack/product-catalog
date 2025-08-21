# Architecture and Design Decisions

This document provides a high-level overview of the project's architecture, security model, testing strategy, and performance considerations.

## 🏗️ Architecture Overview

The application follows a classic layered architecture pattern, promoting separation of concerns and maintainability.

### 📁 Package Structure
```bash
com.thedavestack.productcatalog/
├── controller/     \# REST API endpoints with security
├── service/        \# Business logic with transactions  
├── repository/     \# Data access layer (JPA)
├── model/          \# JPA entities with audit fields
├── dto/            \# Request/response objects
├── mapper/         \# Entity-DTO conversion
├── security/       \# JWT authentication & filters
├── validation/     \# Custom validation logic
├── exception/      \# Global error handling
└── config/         \# Spring configuration classes
```

### 🔄 Application Flow
1.  **Authentication**: A client obtains a JWT via the `/api/v1/auth/login` endpoint.
2.  **Authorization**: For subsequent requests, the `JwtAuthenticationFilter` validates the token and sets the security context. Role-based access control is enforced at the controller level.
3.  **Request Processing**: Controllers receive requests, validate DTOs, and delegate to the appropriate service layer method.
4.  **Business Logic**: Services contain the core business logic, manage transactions (`@Transactional`), and perform audit logging.
5.  **Data Access**: Repositories, extending `JpaRepository`, handle database operations, including soft deletes (`@SQLDelete`) and optimistic locking (`@Version`).
6.  **Response**: The application returns standardized DTOs with the appropriate HTTP status codes. Exceptions are handled globally for consistent error responses.

## 🔒 Security Architecture
- **JWT Stateless Authentication**: No server-side session storage, making the application scalable.
- **Role-Based Access Control**: Clear separation of duties between `ADMIN` (full CRUD) and `USER` (read-only) roles.
- **Input Validation**: Multi-layer validation using Bean Validation annotations (`@Valid`) and custom validators for business rules.
- **Developer-Friendly Errors**: 401 (Unauthorized) and 404 (Not Found) errors provide helpful links and guidance to the developer.

## 🧪 Testing Strategy

The application follows a comprehensive 3-tier testing approach to ensure reliability and correctness.

### ⚡ **Unit Tests** (14 tests)
- **Focus**: Isolate and test individual business logic components in the `ProductService`.
- **Framework**: JUnit 5 + Mockito. Repositories and other dependencies are mocked.

### 🔗 **Integration Tests** (9 tests)  
- **Focus**: Test the interaction between controllers, services, security, and the database.
- **Framework**: `@SpringBootTest` + `MockMvc`.
- **Database**: An ephemeral PostgreSQL database is spun up for each test run using Testcontainers.

### 🌐 **E2E Tests** (10 tests)
- **Focus**: Verify complete application workflows from the perspective of an API client.
- **Framework**: RestAssured + Testcontainers.
- **Process**: Tests make real HTTP requests to the application running in a Docker container, including the full JWT login flow.

## 📈 Performance & Observability

### Performance Features
- **Database Indexing**: Strategic indexes on `sku`, `category`, and `stock_quantity` to speed up common queries.
- **Connection Pooling**: Uses HikariCP, a high-performance JDBC connection pool.
- **Asynchronous Processing**: Audit logging is performed asynchronously to avoid blocking the main request thread.
- **Soft Deletes**: Preserves data integrity for auditing without the performance penalty of `DELETE` operations on indexed tables.

### Monitoring & Observability
- **Enhanced Health Checks**: `/actuator/health` includes custom checks for database connectivity and product inventory status.
- **Custom Business Metrics**: `/actuator/productmetrics` provides key business indicators (e.g., total products, stock levels).
- **Audit Trail Access**: `/actuator/audit` exposes a history of all CRUD operations for traceability.
- **Structured Logging**: Logs are formatted for easy parsing by container-based log aggregation systems.

## 🐳 Container Security Features
- **Non-Root User**: The application runs as a non-root user inside the container to reduce the attack surface.
- **Resource Limits**: Docker Compose files define memory and CPU limits to prevent resource exhaustion.
- **Secret Management**: All secrets (database passwords, JWT keys) are injected via environment variables, not hardcoded.