# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running
- **Build project**: `mvn clean install`
- **Run application**: `mvn spring-boot:run`
- **Run with Docker**: `docker compose up` (recommended for development)

### Testing
- **Unit tests only**: `mvn test`
- **Integration tests**: `mvn verify -Pintegration-tests`
- **End-to-end tests**: `mvn verify -Pe2e-tests`
- **All tests**: `mvn verify -Pintegration-tests && mvn verify -Pe2e-tests`

### Code Quality
- **Format code**: `mvn spotless:apply`
- **Check formatting**: `mvn spotless:check`

## Architecture Overview

This is a Spring Boot 3.5.4 REST API for product catalog management with the following key architectural patterns:

### Package Structure
- **Controller Layer** (`controller/`): REST endpoints following `/api/v1/products` pattern
- **Service Layer** (`service/`): Business logic with transactional operations
- **Repository Layer** (`repository/`): JPA data access using Spring Data
- **DTO Layer** (`dto/`): Request/response objects for API boundaries
- **Mapper Layer** (`mapper/`): Entity-DTO conversion using MapStruct pattern
- **Exception Layer** (`exception/`): Custom exceptions with global error handling

### Key Components
- **Product Entity**: JPA entity with UUID primary key, unique SKU constraint
- **Database**: PostgreSQL with Flyway migrations in `src/main/resources/db/migration/`
- **Testing Strategy**: Three-tier testing (Unit, Integration with Testcontainers, E2E with RestAssured)
- **API Documentation**: Swagger/OpenAPI via springdoc-openapi

### Configuration
- **Profiles**: Default, `test`, `test-e2e`
- **Database**: Configured via environment variables for Docker deployment
- **Actuator**: Health checks enabled at `/actuator/health`

## Development Notes

### Database Schema
- Flyway manages migrations automatically on startup
- Products table has unique constraint on SKU field
- Uses UUID for primary keys, BigDecimal for prices

### Testing Patterns
- Unit tests use `@ExtendWith(MockitoExtension.class)`
- Integration tests extend `BaseIntegrationTest` with Testcontainers
- E2E tests extend `BaseE2ETest` with full Spring context and RestAssured

### Error Handling
- Custom exceptions: `ProductNotFoundException`, `DuplicateSkuException`
- Global exception handler provides consistent API responses