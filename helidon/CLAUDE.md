# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build and Test
```bash
# Clean build with dependencies
mvn clean package

# Run the application (requires PostgreSQL running)
java -jar target/product-catalog-helidon.jar

# Build native image with GraalVM
mvn -Pnative-image install -DskipTests

# Build custom runtime image with jlink
mvn package -Pjlink-image

# Code formatting check
mvn spotless:check

# Apply code formatting
mvn spotless:apply

# Run tests (unit tests only)
mvn test

# Run all tests (unit + integration)
mvn verify

# Run only unit tests
mvn test -Dtest="*Test"

# Run only integration tests
mvn test -Dtest="*IT"
```

### Database Setup
```bash
# Start PostgreSQL with Docker Compose
docker-compose up -d

# Stop database
docker-compose down

# Reset database (removes data volume)
docker-compose down -v
```

### Docker Operations
```bash
# Build Docker image
docker build -t product-catalog-helidon .

# Run Docker image
docker run --rm -p 8080:8080 product-catalog-helidon:latest

# Build native Docker image
docker build -f Dockerfile.native -t product-catalog-helidon:native .
```

## Project Overview

This is a **Product Catalog Service** built with Helidon MP as a direct comparison to Spring Boot implementations, demonstrating Helidon's capabilities in microservices architecture. The service provides centralized product information management with cloud-native, container-friendly deployment.

## Architecture Overview

### Clean Architecture Layers
- **Resource Layer (REST Controllers)**: HTTP request handling and input validation ✅ **COMPLETED**
- **Service Layer**: Business logic and orchestration ✅ **COMPLETED**
- **Repository Layer**: Data access abstraction with Helidon DB Client ✅ **COMPLETED**

### Framework Stack
- **Helidon MP 4.2.5**: MicroProfile-compliant microservice framework
- **Jakarta EE**: Enterprise Java specifications (CDI, JAX-RS, JSON-B)
- **Helidon DB Client**: Reactive database client for PostgreSQL
- **PostgreSQL 17.5**: Database with manual schema management
- **MicroProfile Config**: External configuration management

### Project Structure
- `src/main/java/com/thedavestack/productcatalog/`
  - `model/`: Data objects (Product record) ✅ **COMPLETED**
  - `repository/`: Data access layer using Helidon DB Client ✅ **COMPLETED**  
  - `service/`: Business logic and orchestration ✅ **COMPLETED**
  - `resource/`: JAX-RS REST endpoints ✅ **COMPLETED**
  - `dto/`: Data Transfer Objects for API requests/responses ✅ **COMPLETED**
  - `exception/`: Exception mappers for structured error handling ✅ **COMPLETED**
  - `config/`: CDI configuration for database connectivity ✅ **COMPLETED**
- `db-init/schema.sql`: PostgreSQL schema initialization ✅ **COMPLETED**
- `docs/api-design-product-catalog-helidon.md`: Complete API specification ✅ **COMPLETED**
- `.clinerules/memory-bank/`: Project context and progress tracking

### Key Design Decisions
1. **Data Model**: Java Records for immutable Product objects with String UUIDs
2. **Database Access**: Helidon DB Client with manual SQL (lightweight alternative to JPA)
3. **Async Operations**: CompletableFuture-based repository methods for reactive programming
4. **REST API**: JAX-RS with async CompletionStage pattern and structured error responses
5. **Input Validation**: Jakarta Bean Validation with custom validation messages
6. **Exception Handling**: Dedicated exception mappers for all error scenarios
7. **Schema Management**: Idempotent SQL scripts with `IF NOT EXISTS` 
8. **Containerization**: Supports both JVM and GraalVM native images for cloud deployment
9. **Clean Architecture**: Strict separation of concerns across layers

### Database Configuration
- Connection details configured in `microprofile-config.properties`
- Default connection: `jdbc:postgresql://localhost:5432/product_catalog_db`
- Connection pooling configured with 20 max connections, 5 minimum idle

### Code Style
- Google Java Format (AOSP style) enforced by Spotless plugin
- Import ordering: java, javax, org, com
- Wildcard imports removed automatically

### API Endpoints ✅ **IMPLEMENTED**
- `POST /api/v1/products` - Create single product
- `POST /api/v1/products/batch-create` - Batch create products  
- `GET /api/v1/products/{id}` - Get product by ID
- `GET /api/v1/products` - List products with pagination and category filtering
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Delete product
- `GET /api/v1/products/export?format=json` - Export products

### API Features
- **Async Processing**: All endpoints use CompletionStage for non-blocking operations
- **Input Validation**: Jakarta Bean Validation with custom error messages
- **Structured Errors**: Comprehensive error responses with timestamps and request paths
- **Pagination Support**: Page/size parameters with total counts and metadata
- **Category Filtering**: Filter products by category in list endpoint
- **Export Functionality**: JSON export with download headers

### Health and Monitoring
- Health checks available at `/health`
- Metrics available at `/metrics` (Prometheus and JSON formats)
- OpenAPI documentation planned

## Development Notes

### Testing Strategy ✅ **IMPLEMENTED**
- **Unit Tests**: JUnit 5 with Hamcrest matchers and Mockito framework
- **Integration Tests**: Helidon MP Testing framework with Testcontainers PostgreSQL
- **Test Coverage**: 38 unit tests + 13 integration tests covering all functionality
- **Database Testing**: Testcontainers for real PostgreSQL integration testing
- **Test Isolation**: Proper cleanup and isolation between test runs

### Test Structure
```
src/test/java/com/thedavestack/productcatalog/
├── service/
│   └── ProductServiceTest.java           # Unit tests (38 methods)
└── resource/  
    └── ProductResourceIT.java            # Integration tests (13 methods)

src/test/resources/
├── META-INF/
│   └── microprofile-config.properties   # Test configuration
└── schema-test.sql                       # Test database schema
```

### Test Dependencies
- **mockito-core & mockito-junit-jupiter** (v5.7.0): Unit testing with mocks
- **testcontainers-junit-jupiter & testcontainers-postgresql** (v1.19.3): Integration testing
- **hamcrest-all**: Readable assertions and pattern matching
- **helidon-microprofile-testing-junit5**: Helidon test framework

## Implementation Progress

### ✅ Completed Components
- **Data Model**: Product record with comprehensive field mapping
- **Repository Layer**: Full CRUD operations with Helidon DB Client
  - Async operations with CompletableFuture
  - Type-safe field queries with enum
  - Batch operations with transactional integrity
  - Pagination support
- **Service Layer (ProductService)**: Complete business logic implementation
  - SKU auto-generation with category-based prefixes
  - Input validation and business rules
  - Exception handling for domain-specific errors
  - Async operations throughout
- **REST Layer (ProductResource)**: Complete JAX-RS endpoints implementation
  - All MVP endpoints (create, read, update, delete, batch, export)
  - Async CompletionStage pattern
  - Input validation with Jakarta Bean Validation
  - Comprehensive error handling with exception mappers
- **DTO Architecture**: Clean API contracts
  - ProductResponse, CreateProductRequest, BatchCreateProductRequest, ProductListResponse
  - JSON-B annotations for proper serialization
  - Validation annotations with custom messages
- **Exception Handling**: Comprehensive error management
  - 6 dedicated exception mappers covering all error scenarios
  - Structured error responses with timestamps and paths
  - Proper HTTP status code mapping
- **Database Configuration**: CDI-managed connection pooling
- **Schema Management**: Idempotent PostgreSQL table creation
- **Project Structure**: Complete clean architecture implementation
- **Unit Tests**: Complete JUnit 5 test suite with Mockito ✅ **COMPLETED**
  - ProductServiceTest: 38 comprehensive test methods
  - Complete business logic coverage with mocked dependencies
  - Validation, error scenarios, and SKU generation testing
  - Pattern matching for SKU format validation
- **Integration Tests**: Helidon MP Testing with Testcontainers ✅ **COMPLETED**
  - ProductResourceIT: 13 end-to-end integration tests
  - Real PostgreSQL database testing with Testcontainers
  - All REST endpoints tested with HTTP clients
  - Database persistence and cleanup verification
- **Test Configuration**: Complete test infrastructure ✅ **COMPLETED**
  - Test-specific microprofile configuration
  - Database schema initialization for tests
  - Proper test isolation and cleanup mechanisms

### 📋 Remaining Components (Next Priority: task-12/14)
- **E2E Tests**: RestAssured with full Docker stack (task-14)
- **OpenAPI Documentation**: Swagger/OpenAPI 3.0 specification
- **Health Checks**: Custom health indicators
- **Native Image**: GraalVM compilation optimizations

## Development Guidelines

### Code Standards
- **Architecture**: Follow clean architecture patterns with clear layer separation
- **Testing**: Test-driven development where applicable
- **Documentation**: Maintain comprehensive documentation for all features
- **Git Flow**: Use conventional commits and feature branches

### Memory Bank Integration
This project uses Cline's Memory Bank system for context persistence across sessions. Key files:
- `.clinerules/memory-bank/activeContext.md`: Current work focus and recent changes
- `.clinerules/memory-bank/progress.md`: Implementation status tracking
- `.clinerules/memory-bank/systemPatterns.md`: Architecture decisions

Always run `mvn spotless:check` before committing to ensure code formatting compliance.