# Product Catalog Service Design (Helidon)

## 1. Purpose

To create a robust and modern RESTful service for product catalog management using the Helidon MP framework, for direct comparison with its Spring Boot counterpart.

## 2. Key Decisions

- **Framework**: Helidon MP 4.x (latest stable version), using Jakarta EE & MicroProfile standards.
- **Database Access**: Helidon DB Client will be used for database interactions, implying manual, programmatic SQL query construction.
- **Schema Management**: A manual SQL script ('schema.sql') will be used to initialize the database schema, executed by the PostgreSQL container on startup.
- **Project Scaffolding**: The Helidon CLI ('helidon init') will be used to generate the initial project structure.
- **Data Model**: The 'Product' class will be a simple Java Record, without persistence annotations.
- **Health Checks**: The application will expose a '/health' endpoint using Helidon Health.
- **Containerization**: The application will be packaged as a GraalVM native executable inside a minimal Docker container.
- **SKU Logic**: SKUs will be auto-generated ('[NNN]-######') if not provided and validated against a specific format if they are.
- **Atomicity**: Bulk creation operations will be transactional.
- **E2E Testing**: A dedicated Maven module ('e2e-tests') will be used with Testcontainers and RestAssured to test the full 'docker-compose' stack.

## 3. Data Model

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

## 4. API Endpoints

| Method | Path                               | Description                           |
| :----- | :--------------------------------- | :------------------------------------ |
| POST   | /api/v1/products                   | Creates a single new product.         |
| POST   | /api/v1/products/batch-create      | Creates multiple products atomically. |
| GET    | /api/v1/products/{id}              | Retrieves a single product by its ID. |
| GET    | /api/v1/products                   | Retrieves a paginated list of products. |
| GET    | /api/v1/products/export?format=json| Exports all products to a JSON file.  |
| PUT    | /api/v1/products/{id}              | Updates an existing product.          |
| DELETE | /api/v1/products/{id}              | Deletes a product.                    |
