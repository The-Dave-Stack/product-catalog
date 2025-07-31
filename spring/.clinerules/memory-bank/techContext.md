# Tech Context: Stack and Tooling

## Core Technologies
- **Language**: Java 21
- **Framework**: Spring Boot 3.5.4
- **Build Tool**: Maven
- **Database**: PostgreSQL 17.5
- **Schema Migration**: Flyway
- **Containerization**: Docker

## Key Dependencies
- `spring-boot-starter-web`: For building RESTful APIs.
- `spring-boot-starter-data-jpa`: For data access using JPA.
- `spring-boot-starter-actuator`: For monitoring and health checks.
- `spring-boot-starter-validation`: For data validation.
- `postgresql`: JDBC driver for PostgreSQL.
- `lombok`: To reduce boilerplate code.
- `spring-boot-starter-test`: For unit and integration testing.
- `testcontainers`: For E2E testing with Docker.

## Development Environment
- **Containerization**: Docker and Docker Compose are used to manage the PostgreSQL database instance, ensuring a consistent and reproducible development environment.
- **Code Formatting**: Spotless Maven Plugin with `google-java-format`.
- **IDE**: Any modern IDE supporting Maven and Java (e.g., IntelliJ IDEA, VS Code).

## Project Paths
- **Git Repository Root**: `/home/kratos/Development/Github/The-Dave-Stack/product-catalog`
- **Spring Project Root**: `/home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring`
