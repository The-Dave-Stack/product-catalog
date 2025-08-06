# Project Brief

## Project Name
Product Catalog Service (Helidon)

## Purpose
To develop a robust and modern RESTful service for product catalog management using the Helidon MP framework. This project serves as a direct comparison with a Spring Boot counterpart, focusing on demonstrating Helidon's capabilities in a microservices context.

## Core Requirements
- **API Endpoints**: Implement standard CRUD operations for products (create, retrieve, update, delete), including batch creation and paginated listing.
- **Data Persistence**: Utilize PostgreSQL for data storage, with schema management via manual SQL scripts.
- **Containerization**: Package the application as a GraalVM native executable within a minimal Docker container.
- **Testing**: Comprehensive unit, integration, and end-to-end tests using Testcontainers and RestAssured.
- **Documentation**: Maintain clear API design documentation and project README.

## Key Technologies
- **Backend**: Helidon MP 4.x (Java, Jakarta EE, MicroProfile)
- **Database**: PostgreSQL
- **Containerization**: Docker, GraalVM
- **Testing**: JUnit, Mockito, Testcontainers, RestAssured
- **Build Tool**: Maven

## Goals
- Provide a high-performance, cloud-native product catalog service.
- Demonstrate best practices for Helidon MP application development.
- Serve as a reference implementation for future Helidon projects.
- Ensure maintainability and extensibility through clean architecture and comprehensive testing.

## Last Updated
2025-08-06 by Cline (Model: claude-3-opus, Task: Initialized memory bank)
