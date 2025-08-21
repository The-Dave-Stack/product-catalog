# API Reference Guide

This document provides a detailed reference for all available API endpoints.

**Base URL**: `/api/v1`

## 🔐 Authentication

### Default Credentials:
- **Admin**: `admin` / `admin123` (Full CRUD access)
- **User**: `user` / `user123` (Read-only access)

| Method | Path                    | Description                      | Authorization |
|--------|-------------------------|----------------------------------|---------------|
| `POST` | `/auth/login`    | Authenticate and get JWT token   | Public        |

## 📦 Product Management
| Method | Path                           | Description                           | Authorization |
|--------|--------------------------------|---------------------------------------|---------------|
| `POST` | `/products`             | Create a new product                  | ADMIN         |
| `GET`  | `/products/{id}`        | Retrieve a product by ID              | USER/ADMIN    |
| `GET`  | `/products`             | Retrieve products with pagination     | USER/ADMIN    |
| `GET`  | `/products/low-stock`   | Get products below minimum stock      | USER/ADMIN    |
| `PUT`  | `/products/{id}`        | Update an existing product            | ADMIN         |
| `DELETE`| `/products/{id}`       | Soft delete a product                 | ADMIN         |

### 🔍 Advanced Query Parameters
The `GET /products` endpoint supports the following query parameters for filtering, sorting, and pagination:

- `page` (integer): The page number to retrieve (default: `0`).
- `size` (integer): The number of items per page (default: `20`).
- `sortBy` (string): The field to sort by (default: `name`).
- `sortDir` (string): The sort direction, `asc` or `desc` (default: `asc`).
- `category` (string): Filter by a specific product category (e.g., `ELECTRONICS`).
- `minPrice` (number): Filter for products with a price greater than or equal to this value.
- `maxPrice` (number): Filter for products with a price less than or equal to this value.
- `name` (string): Search for a product by its name (case-insensitive partial match).

## 📊 Health & Documentation  
| Method | Path                           | Description                           | Authorization |
|--------|--------------------------------|---------------------------------------|---------------|
| `GET`  | `/actuator/health`             | Application health status with custom product health | Public |
| `GET`  | `/actuator/info`               | Enhanced application information with features | USER/ADMIN |
| `GET`  | `/actuator/metrics`            | Standard Spring Boot metrics          | USER/ADMIN |
| `GET`  | `/actuator/productmetrics`     | Custom product catalog metrics        | USER/ADMIN |
| `GET`  | `/actuator/audit`              | Audit log summary and recent entries  | USER/ADMIN |
| `GET`  | `/actuator/audit/{entityId}`   | Audit logs for specific entity        | USER/ADMIN |
| `GET`  | `/swagger-ui/index.html`       | Interactive API documentation         | Public |
| `GET`  | `/v3/api-docs`                 | OpenAPI specification (JSON)         | Public |

## 💡 Example Usage

Replace `YOUR_JWT_TOKEN` with a valid token obtained from the `/auth/login` endpoint.

```bash
# Get JWT token for admin user
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Create a new product (requires ADMIN token)
curl -X POST http://localhost:8080/api/v1/products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Gaming Mouse","price":79.99,"category":"ELECTRONICS","stockQuantity":150, "minStockLevel":20}'

# Get all products from the ELECTRONICS category priced between 50 and 100
curl "http://localhost:8080/api/v1/products?category=ELECTRONICS&minPrice=50&maxPrice=100" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Check application health (publicly accessible)
curl http://localhost:8080/actuator/health

# Get custom product metrics (requires authentication)
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/actuator/productmetrics

# Get audit log summary for all entities
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/actuator/audit
```