# Tech Context: Enterprise Stack and Tooling

## Core Technologies - Enterprise Grade
- **Language**: Java 21 (LTS)
- **Framework**: Spring Boot 3.5.4 with Spring Security
- **Build Tool**: Maven 3.9+
- **Database**: PostgreSQL 17.5 with advanced features
- **Schema Migration**: Flyway for versioned database evolution
- **Authentication**: JWT (JSON Web Tokens) with JJWT library
- **Containerization**: Docker & Docker Compose

## Key Dependencies - Enhanced

### Core Spring Boot Starters
- `spring-boot-starter-web`: RESTful API with advanced features
- `spring-boot-starter-data-jpa`: Data access with soft deletes and indexing
- `spring-boot-starter-security`: JWT authentication and role-based authorization
- `spring-boot-starter-actuator`: Production monitoring and health checks
- `spring-boot-starter-validation`: Comprehensive input validation with custom validators

### Database & Migration
- `postgresql`: JDBC driver for PostgreSQL
- `flyway-core` & `flyway-database-postgresql`: Database versioning and migration
- **HikariCP**: High-performance connection pooling (included in Spring Boot)

### Security & Authentication
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: Complete JWT implementation
- **Spring Security**: Role-based authorization and method security

### Documentation & API
- `springdoc-openapi-starter-webmvc-ui`: Swagger UI with security schemas
- **Lombok**: Boilerplate code reduction

### Testing Infrastructure - Comprehensive
- `spring-boot-starter-test`: JUnit 5, Mockito, AssertJ
- `testcontainers-junit-jupiter`: Container-based integration testing
- `testcontainers-postgresql`: PostgreSQL containers for testing
- `rest-assured`: Advanced API testing with authentication flow

### Code Quality
- **Spotless Maven Plugin**: Automated code formatting with Google Java Format
- **Maven Surefire Plugin**: Unit test execution
- **Maven Failsafe Plugin**: Integration and E2E test execution

## Development Environment - Production Ready

### Containerization
- **Docker & Docker Compose**: Full application stack management
- **PostgreSQL Container**: Database isolation and consistency
- **Application Container**: Production-ready Docker image
- **Health Checks**: Docker health check integration

### Code Quality & Formatting
- **Spotless**: Enforced code formatting with Google Java Format
- **Import Organization**: Consistent import ordering
- **IDE Integration**: Compatible with IntelliJ IDEA, VS Code, Eclipse

### Environment Profiles
- **Default Profile**: Development configuration
- **Test Profile**: Unit and integration testing
- **Test-E2E Profile**: End-to-end testing configuration
- **Production Profile**: Production-optimized settings

## Testing Infrastructure - 28+ Tests

### Unit Testing (14 tests)
- **Framework**: JUnit 5 with Mockito
- **Focus**: Service layer business logic isolation
- **Coverage**: ProductService methods with comprehensive scenarios
- **Performance**: Fast execution (~3 seconds)

### Repository Testing (4 tests)
- **Framework**: Spring Boot Test with `@DataJpaTest`
- **Database**: Testcontainers with PostgreSQL
- **Focus**: JPA operations, constraints, and custom queries
- **Coverage**: CRUD operations and business rule validation

### Integration Testing (9 tests)
- **Framework**: SpringBootTest with MockMvc
- **Security**: JWT authentication flow testing
- **Focus**: Controller layer with security integration
- **Coverage**: Protected endpoints and role-based authorization

### E2E Testing (10 tests)
- **Framework**: RestAssured with Testcontainers
- **Authentication**: Complete JWT login and API access flow
- **Focus**: Full application stack testing with real HTTP requests
- **Coverage**: CRUD operations, error handling, and business scenarios

## Performance & Scalability Features

### Database Optimization
- **Strategic Indexing**: Indexes on `sku`, `category`, `stock_quantity`, `deleted`
- **Connection Pooling**: HikariCP with optimized settings
- **Query Optimization**: JPA queries optimized with proper indexes
- **Lazy Loading**: JPA relationships loaded on demand

### Processing Optimization
- **Async Processing**: Background audit logging with `@Async`
- **Connection Management**: Efficient database connection usage
- **Soft Delete Queries**: Optimized `@Where` clauses for active data filtering
- **Pagination**: Built-in pagination for large dataset handling

## Security Configuration

### JWT Implementation
- **Token Generation**: Stateless JWT tokens with role-based claims
- **Token Validation**: Custom authentication filter with proper error handling
- **Expiration Management**: Configurable token expiration (default: 24 hours)
- **Role-Based Access**: ADMIN (full CRUD) vs USER (read-only) permissions

### Security Headers & CORS
- **Security Configuration**: Production-ready security headers
- **CORS Configuration**: Cross-origin request handling
- **Authentication Entry Points**: Proper 401/403 error responses

## Production Configuration

### Monitoring & Health Checks
- **Spring Boot Actuator**: Health endpoints for load balancer integration
- **Custom Health Indicators**: Application-specific health checks
- **Metrics Collection**: Application metrics for monitoring
- **Docker Health Checks**: Container health validation

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Environment profile selection
- `SPRING_DATASOURCE_*`: Database connection configuration
- `APP_JWT_SECRET`: JWT token signing key
- `APP_JWT_EXPIRATION`: Token expiration in seconds

### Logging Configuration
- **Structured Logging**: JSON logs for centralized aggregation
- **Debug Logging**: Comprehensive debug information in tests
- **Production Logging**: Optimized logging levels for production

## Project Structure - Enhanced

### Core Application Paths
- **Git Repository Root**: `/home/kratos/Development/Github/The-Dave-Stack/product-catalog`
- **Spring Application**: Root directory (moved from `spring/` subdirectory)
- **Main Source**: `src/main/java/com/thedavestack/productcatalog/`
- **Test Source**: `src/test/java/com/thedavestack/productcatalog/`

### Package Organization
- `controller/`: REST endpoints with security (`ProductController`, `AuthController`)
- `service/`: Business logic (`ProductService`, `AuditService`)
- `repository/`: Data access (`ProductRepository`, `AuditLogRepository`)
- `security/`: JWT authentication (`JwtUtil`, `JwtAuthenticationFilter`, etc.)
- `model/`: JPA entities (`Product`, `AuditLog`, `Category`)
- `dto/`: Request/response objects with validation
- `validation/`: Custom validators (`SkuValidator`, `@ValidSku`)
- `exception/`: Error handling (`GlobalExceptionHandler`, custom exceptions)
- `config/`: Application configuration (`SecurityConfig`, `AsyncConfig`, etc.)

### Resource Organization
- `src/main/resources/db/migration/`: Flyway migration scripts (3 migrations)
- `src/main/resources/`: Application properties for different profiles
- `src/test/resources/`: Test-specific configuration files

## Development Workflow

### Build & Run Commands
- **Build**: `mvn clean install`
- **Run**: `mvn spring-boot:run`
- **Docker**: `docker compose up`
- **Tests**: `mvn verify` (runs all 28+ tests)
- **Format**: `mvn spotless:apply`

### Authentication Testing
- **Admin**: `admin` / `admin123` (full CRUD access)
- **User**: `user` / `user123` (read-only access)
- **Login**: `POST /api/v1/auth/login`
- **Protected APIs**: Require `Authorization: Bearer <JWT_TOKEN>` header