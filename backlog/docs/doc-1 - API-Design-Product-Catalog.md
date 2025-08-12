---
id: doc-1
title: API Design - Product Catalog
type: other
created_date: '2025-07-28'
---

# API Design - Product Catalog

## Purpose
To create a robust and modern RESTful service for product catalog management, serving as a portfolio piece and a baseline for framework comparisons.

## Key Decisions
- **Technology Stack**: Java 21 (LTS), Spring Boot 3.x, Maven.
- **Database**: PostgreSQL (image 'postgres:17.5-alpine'), managed via Docker Compose.
- **Schema Management**: Flyway will be used. Hibernate's 'ddl-auto' will be set to 'validate'.
- **Entity IDs**: Primary keys will be UUIDs.
- **SKU Logic**: Auto-generated ('[NNN]-######') if not provided; validated against a format if provided.
- **Atomicity**: Bulk creation will be transactional.
- **Health Checks**: A '/actuator/health' endpoint will be exposed.
- **E2E Testing**: A dedicated Maven module will test the full 'docker-compose' stack with Testcontainers.

## Data Model
| Field       | Data Type      | Constraints                  | Description                                 |
| :---------- | :------------- | :--------------------------- | :------------------------------------------ |
| id          | String         | Primary Key, UUID            | Unique identifier for the product.          |
| sku         | String         | Not Null, Unique             | Stock Keeping Unit. Auto-generated if null. |
| name        | String         | Not Null                     | Product's name.                             |
| description | String         |                              | Detailed product description.               |
| price       | BigDecimal     | Not Null                     | Price of the product.                       |
| category    | String         |                              | Simple text-based product category.         |
| createdAt   | Instant        |                              | Timestamp of creation (auto-managed).     |
| updatedAt   | Instant        |                              | Timestamp of last update (auto-managed).  |

## API Endpoints
| Method | Path                               | Description                           |
| :----- | :--------------------------------- | :------------------------------------ |
| POST   | /api/v1/products                   | Creates a single new product.         |
| POST   | /api/v1/products/batch-create      | Creates multiple products atomically. |
| GET    | /api/v1/products/{id}              | Retrieves a single product by its ID. |
| GET    | /api/v1/products                   | Retrieves a paginated list of products. |
| GET    | /api/v1/products/export?format=json| Exports all products to a JSON file.  |
| PUT    | /api/v1/products/{id}              | Updates an existing product.          |
| DELETE | /api/v1/products/{id}              | Deletes a product.                    |
