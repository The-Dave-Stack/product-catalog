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

# Run tests
mvn test
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
- **Resource Layer (REST Controllers)**: HTTP request handling and input validation
- **Service Layer**: Business logic and orchestration (planned - task-9)
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
  - `config/`: CDI configuration for database connectivity ✅ **COMPLETED**
- `db-init/schema.sql`: PostgreSQL schema initialization ✅ **COMPLETED**
- `docs/api-design-product-catalog-helidon.md`: Complete API specification ✅ **COMPLETED**
- `.clinerules/memory-bank/`: Project context and progress tracking

### Key Design Decisions
1. **Data Model**: Java Records for immutable Product objects with String UUIDs
2. **Database Access**: Helidon DB Client with manual SQL (lightweight alternative to JPA)
3. **Async Operations**: CompletableFuture-based repository methods for reactive programming
4. **Schema Management**: Idempotent SQL scripts with `IF NOT EXISTS` 
5. **Containerization**: Supports both JVM and GraalVM native images for cloud deployment
6. **Clean Architecture**: Strict separation of concerns across layers

### Database Configuration
- Connection details configured in `microprofile-config.properties`
- Default connection: `jdbc:postgresql://localhost:5432/product_catalog_db`
- Connection pooling configured with 20 max connections, 5 minimum idle

### Code Style
- Google Java Format (AOSP style) enforced by Spotless plugin
- Import ordering: java, javax, org, com
- Wildcard imports removed automatically

### API Endpoints (Planned)
- `POST /api/v1/products` - Create single product
- `POST /api/v1/products/batch-create` - Batch create products
- `GET /api/v1/products/{id}` - Get product by ID
- `GET /api/v1/products` - List products with pagination
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Delete product
- `GET /api/v1/products/export?format=json` - Export products

### Health and Monitoring
- Health checks available at `/health`
- Metrics available at `/metrics` (Prometheus and JSON formats)
- OpenAPI documentation planned

## Development Notes

### Testing Strategy
- JUnit 5 with Hamcrest matchers
- Helidon MP Testing framework for integration tests
- Planned: Testcontainers for E2E testing with full Docker stack

## Implementation Progress

### ✅ Completed Components
- **Data Model**: Product record with comprehensive field mapping
- **Repository Layer**: Full CRUD operations with Helidon DB Client
  - Async operations with CompletableFuture
  - Type-safe field queries with enum
  - Batch operations with transactional integrity
  - Pagination support
- **Database Configuration**: CDI-managed connection pooling
- **Schema Management**: Idempotent PostgreSQL table creation
- **Project Structure**: Clean architecture foundation

### 🚧 In Progress (Next: task-9)
- **Service Layer (ProductService)**: Business logic implementation
- **SKU Generation**: Auto-generation logic for products without SKUs

### 📋 Planned Components  
- **REST Layer (ProductResource)**: JAX-RS endpoints implementation
- **Input Validation**: Request validation and error handling
- **Unit Tests**: JUnit 5 with Mockito for all layers
- **Integration Tests**: Testcontainers with PostgreSQL
- **E2E Tests**: RestAssured with full Docker stack
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