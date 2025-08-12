# System Patterns: Enterprise Architecture and Design

## High-Level Architecture - Enterprise Grade
The application follows an **enterprise-grade layered architecture** with security, performance optimization, and comprehensive patterns:

1. **Presentation Layer (`ProductController` + `AuthController`)**: Handles HTTP requests with JWT authentication, validates data using DTOs with comprehensive validation, and delegates to the service layer. Implements role-based authorization.

2. **Security Layer (`JwtAuthenticationFilter`, `JwtUtil`, `SecurityConfig`)**: Manages JWT-based authentication, role-based authorization, and security policies. Protects all API endpoints with stateless authentication.

3. **Service Layer (`ProductService`, `AuditService`)**: Contains core business logic with transactional operations. Orchestrates data operations, enforces business rules, and manages asynchronous audit logging.

4. **Data Access Layer (`ProductRepository`, `AuditLogRepository`)**: Manages database interactions using Spring Data JPA with advanced patterns like soft deletes, strategic indexing, and optimistic locking.

5. **Configuration Layer (`SecurityConfig`, `AsyncConfig`, `OpenApiConfig`)**: Manages application-wide configurations for security, async processing, and API documentation.

## Key Technical Decisions - Enterprise Patterns

### Security Architecture
- **JWT Stateless Authentication**: No server-side session storage, scalable authentication
- **Role-Based Authorization**: ADMIN (full CRUD) vs USER (read-only) with method-level security
- **Security Filters**: Custom JWT authentication filter with proper exception handling
- **Hardcoded Demo Users**: Development-friendly auth ready for external integration

### Database Patterns
- **Soft Delete Implementation**: Uses `@SQLDelete` and `@Where` annotations for data preservation
- **Optimistic Locking**: `@Version` annotation prevents concurrent update conflicts  
- **Strategic Indexing**: Database indexes on `sku`, `category`, `stock_quantity` for performance
- **Audit Trail**: Complete CRUD operation tracking with asynchronous processing
- **Flyway Migrations**: 3 production-ready migrations with schema versioning

### Performance & Scalability
- **Connection Pooling**: HikariCP with optimized database connections
- **Async Processing**: Background audit logging using `@Async` to avoid request blocking
- **JPA Optimization**: Lazy loading and strategic query patterns
- **Database Indexes**: Strategic indexing on frequently queried fields
- **Pagination**: Built-in pagination support for large datasets

### Testing Architecture
- **3-Tier Testing Strategy**:
  - **Unit Tests (14)**: Service layer isolation using Mockito
  - **Repository Tests (4)**: Data layer with Testcontainers + PostgreSQL
  - **Integration Tests (9)**: MockMvc with JWT authentication flow
  - **E2E Tests (10)**: RestAssured with complete authentication scenarios
- **Authentication Testing**: Complete JWT login and protected endpoint validation

## Component Relationships - Enhanced

### Core Dependencies
- **Controllers** depend on **Services** for business logic
- **Services** depend on **Repositories** for data access  
- **Security Layer** intercepts requests and validates JWT tokens
- **Mappers** handle conversion between **Entities** and **DTOs**
- **Exception Handlers** provide centralized error management
- **Async Services** handle background processing (audit logging)

### Security Flow
- **Authentication**: `AuthController` → `JwtUtil` → JWT Token Generation
- **Authorization**: `JwtAuthenticationFilter` → `JwtUtil` → Role Validation → Endpoint Access
- **Protected Endpoints**: JWT Validation → Role Check → Controller Access

### Data Flow with Audit
- **Create/Update/Delete**: Controller → Service → Repository → Database + Async Audit Log
- **Soft Delete**: Controller → Service → JPA `@SQLDelete` → Logical Deletion + Audit
- **Query Operations**: Controller → Service → Repository → `@Where` Filtered Results

## Critical Implementation Paths

### Authentication Flow
- **Login**: `POST /api/v1/auth/login` → Credential Validation → JWT Generation → Token Response
- **Protected API**: JWT Header → `JwtAuthenticationFilter` → Token Validation → Role Check → Endpoint Access

### Product Operations with Security
- **Create Product**: JWT Validation (ADMIN) → Controller → Service (Business Logic + SKU Generation) → Repository → Database + Audit
- **Read Product**: JWT Validation (USER/ADMIN) → Controller → Service → Repository → Filtered Results (`@Where`)
- **Update Product**: JWT Validation (ADMIN) → Controller → Service (Optimistic Locking) → Repository → Database + Audit
- **Delete Product**: JWT Validation (ADMIN) → Controller → Service → Soft Delete (`@SQLDelete`) + Audit

### Advanced Query Operations
- **Pagination**: Controller → Service → Repository → Page<Product> with sorting
- **Filtering**: Controller → Service → Repository → Specification-based queries with indexes
- **Low Stock**: Controller → Service → Repository → Custom query with stock threshold

### Error Handling Patterns
- **Authentication Errors**: `JwtAuthenticationEntryPoint` → 401 Unauthorized Response
- **Authorization Errors**: Method Security → 403 Forbidden Response  
- **Business Logic Errors**: Custom Exceptions → `GlobalExceptionHandler` → Standardized Error Response
- **Validation Errors**: Bean Validation → `GlobalExceptionHandler` → Field-level Error Details

### Testing Patterns
- **Unit Testing**: Service Layer → Mock Dependencies → Business Logic Validation
- **Integration Testing**: SpringBootTest → MockMvc → JWT Authentication → Full Request Flow
- **E2E Testing**: Testcontainers → RestAssured → Real HTTP → Authentication Flow → API Validation
- **Database Testing**: Testcontainers PostgreSQL → JPA Operations → Transaction Testing

## Enterprise Design Patterns

### Security Patterns
- **JWT Token Pattern**: Stateless authentication with role-based claims
- **Filter Chain Pattern**: Security filters for authentication and authorization
- **Method Security**: `@PreAuthorize` annotations for fine-grained access control

### Data Patterns
- **Soft Delete Pattern**: Logical deletion with `@SQLDelete` for data preservation
- **Audit Pattern**: Automatic tracking of all entity changes
- **Optimistic Locking**: Version-based concurrency control
- **Repository Pattern**: Abstracted data access with Spring Data JPA

### API Patterns
- **DTO Pattern**: Separation of API contracts from internal entities
- **Pagination Pattern**: Standardized page-based data retrieval
- **Error Handling Pattern**: Consistent error responses across all endpoints
- **Validation Pattern**: Multi-layer validation (annotation + business rules)

### Performance Patterns  
- **Connection Pool Pattern**: Database connection optimization with HikariCP
- **Async Processing Pattern**: Background operations for non-critical tasks
- **Index Strategy Pattern**: Strategic database indexing for query optimization
- **Lazy Loading Pattern**: JPA relationships loaded on demand