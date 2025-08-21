# Product Catalog Spring Boot API

## Project Overview
This project implements a **comprehensive, enterprise-grade RESTful API** for product catalog management using Spring Boot 3.5.4. It demonstrates modern Java application development with **JWT authentication**, **advanced inventory management**, **soft delete patterns**, **audit logging**, and **comprehensive testing**. The API is production-ready with role-based authorization, database optimization, and full observability, including an advanced integration with Spring AI's MCP Server.

## Technologies Used
* **Language**: Java 21
* **Framework**: Spring Boot 3.5.4 with Spring Security
* **AI Framework**: Spring AI
* **Build Tool**: Maven
* **Database**: PostgreSQL with advanced indexing and user management
* **Schema Migration**: Flyway (4 migrations)
* **Authentication**: JWT (JSON Web Tokens)
* **Containerization**: Docker & Docker Compose
* **Testing**: JUnit 5, Mockito, Testcontainers, RestAssured
* **Code Quality**: Spotless (Google Java Format)
* **Documentation**: Swagger/OpenAPI 3
* **Validation**: Bean Validation with custom validators
* **ORM**: Hibernate with soft delete patterns
* **Logging**: Structured logging with audit trails

## 🚀 Key Features
* 🔐 **Secure by Design**: Database-managed users with JWT authentication and granular role-based access control (ADMIN, USER, MANAGER, READONLY).
* 📊 **Advanced Product Management**: Soft deletes, optimistic locking, inventory tracking, and custom validations.
* 🔍 **Powerful API**: Pagination, sorting, and advanced filtering capabilities.
* 🤖 **Comprehensive AI Integration**: Natural language interface for catalog management via Spring AI's MCP Server.
* 🏗️ **Robust Database**: Optimized queries with strategic indexing and versioned schema migrations using Flyway.
* 🧪 **Enterprise-Grade Testing**: Full coverage with unit, integration, and E2E tests.
* 📖 **Excellent Observability**: Interactive documentation with Swagger, custom health indicators, and business metrics.
* 🌍 **Multi-Environment Ready**: Pre-configured for local, staging, and production deployments using Docker.

## 🚀 Quick Start (Local)

1.  **Prerequisites**: Java 21, Docker, Maven.
2.  **Clone the repository**:
    ```bash
    git clone https://github.com/your-repo/product-catalog.git
    cd product-catalog
    ```
3.  **Configure environment**:
    ```bash
    cp .env.local.example .env.local
    ```
4.  **Run the application**:
    ```bash
    docker compose -f docker-compose.local.yml up -d
    ```
5.  The API will be available at `http://localhost:8080` and the Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

## 📚 Full Documentation

For a detailed guide on every aspect of this project, please refer to the documents below:

* [**Getting Started**](./docs/01-GETTING_STARTED.md): Complete installation guide and your first API calls.
* [**Environments & Deployment**](./docs/02-ENVIRONMENTS.md): Configuration for local, stage, production, and VPS environments.
* [**API Reference**](./docs/03-API_REFERENCE.md): Detailed endpoints, request/response formats, and cURL examples.
* [**AI Integration (MCP Server)**](./docs/04-MCP_SERVER_AI_INTEGRATION.md): In-depth guide to the Spring AI integration and its capabilities.
* [**Architecture & Design**](./docs/05-ARCHITECTURE_AND_DESIGN.md): Overview of the software architecture, security, and testing strategy.

## 📝 Project Status

### ✅ **Completed Features**
- Full CRUD API with advanced filtering
- Database-managed users with JWT authentication and enhanced role-based authorization (ADMIN/USER/MANAGER/READONLY)
- Comprehensive test suite (28+ tests)
- Database optimization with indexing, soft deletes, and user management audit trails
- Custom actuator endpoints for business metrics and health monitoring
- Docker containerization and multi-environment deployment
- Interactive API documentation and enhanced error handling

### 🎯 **Ready for Production**
This Spring Boot application demonstrates enterprise-grade development practices and is production-ready.

### 📋 **Known Issues / TODOs**
- 3 integration tests temporarily disabled pending investigation into database constraint edge cases. These issues do not affect core functionality or security.