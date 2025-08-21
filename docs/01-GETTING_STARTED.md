# Getting Started Guide

This guide provides everything you need to get the Product Catalog API running on your local machine for development and testing.

## Requirements
* **Docker**: Ensure Docker Desktop or Docker Engine is installed and running.
* **Java 21**: Ensure Java Development Kit (JDK) 21 is installed and configured.
* **Maven**: Ensure Apache Maven is installed and configured.

## 🏠 Local Development Environment Setup

Follow these steps to run the complete application stack locally.

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/your-repo/product-catalog.git
    cd product-catalog
    ```

2.  **Configure Environment Variables**
    Copy the example environment file. The default values are suitable for local development.
    ```bash
    cp .env.local.example .env.local
    ```

3.  **Start the Services**
    This command will build the Java application, start a PostgreSQL database, and run the API.
    ```bash
    docker compose -f docker-compose.local.yml up -d
    ```

4.  **Monitor the Application**
    To view the application and database logs in real-time:
    ```bash
    docker compose -f docker-compose.local.yml logs -f
    ```

5.  **Stop the Services**
    To stop and remove the running containers:
    ```bash
    docker compose -f docker-compose.local.yml down
    ```

## 🛠️ Using Maven for Development

If you prefer to run the application outside of Docker (e.g., from your IDE), you can use Maven. Ensure the database is running via `docker compose -f docker-compose.local.yml up -d postgres-db`.

#### Build the Project
To compile the project and package it into an executable JAR:
```bash
mvn clean install
````

#### Run Tests

```bash
# Unit tests only (14 tests - business logic)
mvn test

# Integration tests (6/9 passing, 3 temporarily disabled)
mvn test -Dtest=*IT

# All tests including integration and E2E (24+ total tests)  
mvn verify

# Code formatting check and apply
mvn spotless:check
mvn spotless:apply
```

#### Run the Application Locally

To run the Spring Boot application directly from Maven:

```bash
mvn spring-boot:run
```

## 🚀 Your First API Calls

Once the application is running, you can interact with it using `cURL` or any API client.

1.  **Get a JWT Token**

    The application uses database-managed users with BCrypt password hashing (V4 migration). Default seeded credentials are:

    **Admin User (full access):**
    ```bash
    curl -X POST http://localhost:8080/api/v1/auth/login \
      -H "Content-Type: application/json" \
      -d '{"username":"admin","password":"admin123"}'
    ```

    **Standard User (read-only + MCP access):**
    ```bash
    curl -X POST http://localhost:8080/api/v1/auth/login \
      -H "Content-Type: application/json" \
      -d '{"username":"user","password":"user123"}'
    ```

    This will return a JSON object with an `accessToken`. Copy the token for the next step.

2.  **Create a New Product (as Admin)**
    Replace `YOUR_JWT_TOKEN` with the token you just received.

    ```bash
    curl -X POST http://localhost:8080/api/v1/products \
      -H "Authorization: Bearer YOUR_JWT_TOKEN" \
      -H "Content-Type: application/json" \
      -d '{"name":"Laptop","price":999.99,"category":"ELECTRONICS","stockQuantity":50}'
    ```

3.  **Retrieve Products**

    ```bash
    curl "http://localhost:8080/api/v1/products?category=ELECTRONICS&minPrice=500&page=0&size=10" \
      -H "Authorization: Bearer YOUR_JWT_TOKEN"
    ```