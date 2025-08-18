# Project Overview

This project is a comprehensive, enterprise-grade RESTful API for product catalog management built with Spring Boot. It features JWT authentication, advanced inventory management, soft delete patterns, audit logging, and a full suite of tests. The application is containerized using Docker and designed for production-ready deployment.

## Technologies

*   **Language**: Java 21
*   **Framework**: Spring Boot 3.5.4 with Spring Security
*   **Build Tool**: Maven
*   **Database**: PostgreSQL
*   **Schema Migration**: Flyway
*   **Authentication**: JWT (JSON Web Tokens)
*   **Containerization**: Docker & Docker Compose
*   **Testing**: JUnit 5, Mockito, Testcontainers, RestAssured
*   **Code Quality**: Spotless (Google Java Format)
*   **Documentation**: Swagger/OpenAPI 3

## Building and Running

### Docker Compose (Recommended)

To run the application and the PostgreSQL database together:

```bash
docker compose up
```

The application will be available at `http://localhost:8080`.

To stop the services:

```bash
docker compose down
```

### Maven

To build the project and run the tests:

```bash
mvn clean install
```

To run the application using Maven (ensure a PostgreSQL database is running):

```bash
mvn spring-boot:run
```

### Testing

*   **Unit Tests**: `mvn test`
*   **Integration Tests**: `mvn test -Dtest=*IT`
*   **All Tests**: `mvn verify`

## Development Conventions

*   **Code Style**: Google Java Format, enforced by the Spotless plugin.
*   **Testing**: The project follows a three-tier testing strategy:
    *   **Unit Tests**: Located in `src/test/java` and end with `Test`.
    *   **Integration Tests**: Located in `src/test/java` and end with `IT`.
    *   **E2E Tests**: Located in `src/test/java` and end with `E2ETest`.
*   **Database Migrations**: Database schema changes are managed with Flyway. Migration scripts are located in `src/main/resources/db/migration`.
*   **API Documentation**: The API is documented using OpenAPI 3. The Swagger UI is available at `/swagger-ui/index.html`.
