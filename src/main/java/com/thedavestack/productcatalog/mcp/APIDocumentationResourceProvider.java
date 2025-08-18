package com.thedavestack.productcatalog.mcp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP Resource Provider for API Documentation. Provides dynamic access to API endpoints,
 * authentication details, request/response examples, and usage guidelines for AI interactions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class APIDocumentationResourceProvider {

    private final ObjectMapper objectMapper;

    /**
     * Provides MCP resource specifications for API documentation.
     *
     * @return List of synchronous resource specifications
     */
    public List<McpServerFeatures.SyncResourceSpecification> getResourceSpecifications() {
        return List.of(
                // API overview resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "api://documentation/overview",
                                "API Overview",
                                "Complete overview of the Product Catalog REST API including base URL, versioning, and general information",
                                "application/json",
                                null),
                        this::getAPIOverviewResource),

                // Authentication documentation resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "api://documentation/authentication",
                                "Authentication Guide",
                                "Detailed authentication documentation including JWT login process, token usage, and security guidelines",
                                "application/json",
                                null),
                        this::getAuthenticationDocumentationResource),

                // Product endpoints documentation resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "api://documentation/endpoints/products",
                                "Product Endpoints",
                                "Complete documentation for all product-related API endpoints with request/response examples",
                                "application/json",
                                null),
                        this::getProductEndpointsResource),

                // Error handling documentation resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "api://documentation/errors",
                                "Error Handling Guide",
                                "Comprehensive error handling documentation including status codes, error formats, and troubleshooting",
                                "application/json",
                                null),
                        this::getErrorHandlingResource),

                // API examples resource
                new McpServerFeatures.SyncResourceSpecification(
                        new McpSchema.Resource(
                                "api://documentation/examples",
                                "API Usage Examples",
                                "Practical examples and use cases for common API operations and workflows",
                                "application/json",
                                null),
                        this::getAPIExamplesResource));
    }

    private McpSchema.ReadResourceResult getAPIOverviewResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> apiOverview =
                    Map.of(
                            "apiInfo",
                                    Map.of(
                                            "name", "Product Catalog REST API",
                                            "version", "1.0.0",
                                            "description",
                                                    "Enterprise-grade Spring Boot REST API for product catalog management",
                                            "baseUrl", "http://localhost:8080",
                                            "documentation", "/swagger-ui/index.html",
                                            "openApiSpec", "/v3/api-docs"),
                            "features",
                                    List.of(
                                            "JWT Authentication with role-based access control",
                                            "Advanced product management with inventory tracking",
                                            "Soft delete patterns for data preservation",
                                            "Comprehensive audit logging",
                                            "Pagination and advanced filtering",
                                            "Low stock alerts and inventory management",
                                            "RESTful design following industry best practices"),
                            "architecture",
                                    Map.of(
                                            "framework", "Spring Boot 3.5.4",
                                            "language", "Java 21",
                                            "database", "PostgreSQL with Flyway migrations",
                                            "security", "Spring Security with JWT",
                                            "testing",
                                                    "JUnit 5, Mockito, Testcontainers, RestAssured",
                                            "documentation", "OpenAPI 3 / Swagger UI"),
                            "supportedFormats", List.of("application/json"),
                            "rateLimiting", "No rate limiting currently implemented",
                            "versioning", "URL path versioning (/api/v1/)",
                            "quickStart",
                                    Map.of(
                                            "step1", "POST /api/v1/auth/login with credentials",
                                            "step2", "Include JWT token in Authorization header",
                                            "step3", "Make API calls to product endpoints",
                                            "step4", "Handle responses and errors appropriately"),
                            "lastUpdated", java.time.Instant.now().toString());

            String jsonContent = objectMapper.writeValueAsString(apiOverview);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get API overview resource", e);
            throw new RuntimeException("Failed to retrieve API overview", e);
        }
    }

    private McpSchema.ReadResourceResult getAuthenticationDocumentationResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> authDoc =
                    Map.of(
                            "authenticationMethod", "JWT (JSON Web Token)",
                            "loginProcess",
                                    Map.of(
                                            "endpoint", "POST /api/v1/auth/login",
                                            "requestBody",
                                                    Map.of(
                                                            "username", "string (required)",
                                                            "password", "string (required)"),
                                            "responseBody",
                                                    Map.of(
                                                            "token", "JWT token string",
                                                            "expiresIn", "86400 seconds (24 hours)",
                                                            "user", "User information object")),
                            "tokenUsage",
                                    Map.of(
                                            "headerName", "Authorization",
                                            "headerValue", "Bearer <jwt-token>",
                                            "example",
                                                    "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."),
                            "userRoles",
                                    Map.of(
                                            "ADMIN",
                                                    Map.of(
                                                            "permissions",
                                                                    "Full CRUD access to all endpoints",
                                                            "endpoints",
                                                                    "All product endpoints + management operations"),
                                            "USER",
                                                    Map.of(
                                                            "permissions",
                                                                    "Read-only access to product data",
                                                            "endpoints",
                                                                    "GET /api/v1/products and related read operations")),
                            "testCredentials",
                                    Map.of(
                                            "admin",
                                                    Map.of(
                                                            "username",
                                                            "admin",
                                                            "password",
                                                            "admin123"),
                                            "user",
                                                    Map.of(
                                                            "username",
                                                            "user",
                                                            "password",
                                                            "user123"),
                                            "note",
                                                    "These are development credentials - change in production"),
                            "securityConsiderations",
                                    List.of(
                                            "Store JWT tokens securely (never in localStorage for production)",
                                            "Handle token expiration gracefully with refresh mechanisms",
                                            "Use HTTPS in production environments",
                                            "Implement proper logout to invalidate tokens",
                                            "Monitor for suspicious authentication attempts"),
                            "errorHandling",
                                    Map.of(
                                            "401",
                                                    "Unauthorized - Invalid credentials or expired token",
                                            "403",
                                                    "Forbidden - Insufficient permissions for requested operation",
                                            "example401Response",
                                                    Map.of(
                                                            "timestamp",
                                                                    "2025-08-18T10:31:36.844264303Z",
                                                            "status", 401,
                                                            "error", "Unauthorized",
                                                            "message", "Authentication required",
                                                            "path", "/api/v1/products")),
                            "codeExamples",
                                    Map.of(
                                            "curl", createCurlAuthExample(),
                                            "javascript", createJavaScriptAuthExample(),
                                            "python", createPythonAuthExample()));

            String jsonContent = objectMapper.writeValueAsString(authDoc);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get authentication documentation resource", e);
            throw new RuntimeException("Failed to retrieve authentication documentation", e);
        }
    }

    private McpSchema.ReadResourceResult getProductEndpointsResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> endpoints =
                    Map.of(
                            "baseUrl", "/api/v1/products",
                            "endpoints",
                                    Map.of(
                                            "createProduct",
                                                    createEndpointDoc(
                                                            "POST",
                                                            "/api/v1/products",
                                                            "ADMIN",
                                                            "Create a new product in the catalog",
                                                            "Product object",
                                                            "Created product with generated ID and SKU"),
                                            "getProduct",
                                                    createEndpointDoc(
                                                            "GET",
                                                            "/api/v1/products/{id}",
                                                            "USER/ADMIN",
                                                            "Retrieve a specific product by ID",
                                                            "Product ID in URL path",
                                                            "Product details"),
                                            "getAllProducts",
                                                    createEndpointDoc(
                                                            "GET",
                                                            "/api/v1/products",
                                                            "USER/ADMIN",
                                                            "Retrieve products with pagination and filtering",
                                                            "Query parameters for pagination and filters",
                                                            "Paginated product list"),
                                            "updateProduct",
                                                    createEndpointDoc(
                                                            "PUT",
                                                            "/api/v1/products/{id}",
                                                            "ADMIN",
                                                            "Update an existing product",
                                                            "Product ID in URL path, updated product object",
                                                            "Updated product details"),
                                            "deleteProduct",
                                                    createEndpointDoc(
                                                            "DELETE",
                                                            "/api/v1/products/{id}",
                                                            "ADMIN",
                                                            "Soft delete a product (marks as inactive)",
                                                            "Product ID in URL path",
                                                            "No content (204)"),
                                            "getLowStockProducts",
                                                    createEndpointDoc(
                                                            "GET",
                                                            "/api/v1/products/low-stock",
                                                            "USER/ADMIN",
                                                            "Retrieve products below minimum stock level",
                                                            "Pagination parameters",
                                                            "List of low-stock products")),
                            "queryParameters",
                                    Map.of(
                                            "pagination",
                                                    Map.of(
                                                            "page",
                                                                    "Page number (0-based, default: 0)",
                                                            "size",
                                                                    "Page size (default: 20, max: 100)",
                                                            "sort", "Sort field (default: name)",
                                                            "direction",
                                                                    "Sort direction (asc/desc, default: asc)"),
                                            "filtering",
                                                    Map.of(
                                                            "category",
                                                                    "Filter by category (ELECTRONICS, BOOKS, etc.)",
                                                            "name",
                                                                    "Filter by product name (partial match)",
                                                            "minPrice", "Filter by minimum price",
                                                            "maxPrice", "Filter by maximum price",
                                                            "active",
                                                                    "Filter by active status (true/false)")),
                            "requestExamples", createRequestExamples(),
                            "responseExamples", createResponseExamples(),
                            "errorScenarios", createErrorScenarios());

            String jsonContent = objectMapper.writeValueAsString(endpoints);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get product endpoints resource", e);
            throw new RuntimeException("Failed to retrieve product endpoints documentation", e);
        }
    }

    private McpSchema.ReadResourceResult getErrorHandlingResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> errorDoc =
                    Map.of(
                            "standardErrorFormat",
                                    Map.of(
                                            "timestamp", "ISO 8601 timestamp of the error",
                                            "status", "HTTP status code",
                                            "error", "HTTP status reason phrase",
                                            "message", "Detailed error message",
                                            "path", "Request path that caused the error",
                                            "errorCode", "Application-specific error code",
                                            "helpLinks", "Array of helpful links (optional)"),
                            "httpStatusCodes",
                                    Map.of(
                                            "200", "OK - Request successful",
                                            "201", "Created - Resource successfully created",
                                            "204",
                                                    "No Content - Request successful, no content to return",
                                            "400",
                                                    "Bad Request - Invalid request data or parameters",
                                            "401",
                                                    "Unauthorized - Authentication required or invalid",
                                            "403", "Forbidden - Insufficient permissions",
                                            "404", "Not Found - Resource does not exist",
                                            "409",
                                                    "Conflict - Duplicate resource (e.g., SKU already exists)",
                                            "422", "Unprocessable Entity - Validation errors",
                                            "500",
                                                    "Internal Server Error - Unexpected server error"),
                            "commonErrors",
                                    Map.of(
                                            "invalidCredentials",
                                                    createErrorExample(
                                                            401, "Invalid username or password"),
                                            "productNotFound",
                                                    createErrorExample(
                                                            404,
                                                            "Product with ID 'abc123' not found"),
                                            "duplicateSku",
                                                    createErrorExample(
                                                            409,
                                                            "Product with SKU 'ELEC-001' already exists"),
                                            "validationError", createValidationErrorExample(),
                                            "insufficientPermissions",
                                                    createErrorExample(
                                                            403,
                                                            "Insufficient permissions to access this resource")),
                            "errorHandlingBestPractices",
                                    List.of(
                                            "Always check HTTP status codes before processing responses",
                                            "Parse error messages for user-friendly display",
                                            "Use helpLinks when available for additional guidance",
                                            "Implement proper retry logic for 5xx errors",
                                            "Log errors appropriately for debugging purposes",
                                            "Handle network timeouts and connection errors"),
                            "troubleshootingGuide",
                                    Map.of(
                                            "401Errors", "Check JWT token validity and format",
                                            "403Errors", "Verify user role and permissions",
                                            "404Errors", "Confirm resource ID and endpoint URL",
                                            "409Errors",
                                                    "Check for duplicate data (especially SKUs)",
                                            "500Errors",
                                                    "Contact support with error details and timestamp"));

            String jsonContent = objectMapper.writeValueAsString(errorDoc);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get error handling resource", e);
            throw new RuntimeException("Failed to retrieve error handling documentation", e);
        }
    }

    private McpSchema.ReadResourceResult getAPIExamplesResource(
            McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        try {
            Map<String, Object> examples =
                    Map.of(
                            "commonWorkflows",
                                    Map.of(
                                            "authenticateAndCreateProduct",
                                                    createWorkflowExample("authenticate-create"),
                                            "searchAndFilterProducts",
                                                    createWorkflowExample("search-filter"),
                                            "updateProductInventory",
                                                    createWorkflowExample("update-inventory"),
                                            "handleLowStockAlerts",
                                                    createWorkflowExample("low-stock")),
                            "integrationPatterns",
                                    Map.of(
                                            "errorHandling",
                                                    "Always wrap API calls in try-catch blocks",
                                            "tokenRefresh",
                                                    "Implement automatic token refresh before expiration",
                                            "rateLimiting",
                                                    "Implement exponential backoff for retries",
                                            "bulkOperations", "Use pagination for large data sets"),
                            "testingExamples",
                                    Map.of(
                                            "unitTests",
                                                    "Test individual API endpoints with mock data",
                                            "integrationTests",
                                                    "Test complete workflows end-to-end",
                                            "loadTests",
                                                    "Test API performance under concurrent load",
                                            "errorTests", "Test error scenarios and edge cases"),
                            "sdkExamples",
                                    Map.of(
                                            "note",
                                            "No official SDKs available - use HTTP client libraries",
                                            "recommendedLibraries",
                                            Map.of(
                                                    "JavaScript", "axios, fetch, node-fetch",
                                                    "Python", "requests, httpx, aiohttp",
                                                    "Java",
                                                            "OkHttp, Apache HttpClient, Spring WebClient",
                                                    "C#", "HttpClient, RestSharp")),
                            "performanceConsiderations",
                                    List.of(
                                            "Use appropriate page sizes for pagination (default: 20)",
                                            "Cache frequently accessed data on client side",
                                            "Use compression for large payloads",
                                            "Monitor response times and optimize queries",
                                            "Implement proper connection pooling"));

            String jsonContent = objectMapper.writeValueAsString(examples);

            return new McpSchema.ReadResourceResult(
                    List.of(
                            new McpSchema.TextResourceContents(
                                    request.uri(), "application/json", jsonContent)));
        } catch (Exception e) {
            log.error("Failed to get API examples resource", e);
            throw new RuntimeException("Failed to retrieve API examples", e);
        }
    }

    // Helper methods for creating documentation structures
    private Map<String, Object> createEndpointDoc(
            String method,
            String path,
            String auth,
            String description,
            String request,
            String response) {
        return Map.of(
                "method", method,
                "path", path,
                "authentication", auth,
                "description", description,
                "requestFormat", request,
                "responseFormat", response);
    }

    private String createCurlAuthExample() {
        return """
                # Login to get JWT token
                curl -X POST http://localhost:8080/api/v1/auth/login \\
                     -H "Content-Type: application/json" \\
                     -d '{"username": "admin", "password": "admin123"}'

                # Use token in subsequent requests
                curl -X GET http://localhost:8080/api/v1/products \\
                     -H "Authorization: Bearer YOUR_JWT_TOKEN"
                """;
    }

    private String createJavaScriptAuthExample() {
        return """
                // Login and store token
                const loginResponse = await fetch('/api/v1/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username: 'admin', password: 'admin123' })
                });
                const { token } = await loginResponse.json();

                // Use token for API calls
                const productsResponse = await fetch('/api/v1/products', {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                """;
    }

    private String createPythonAuthExample() {
        return """
                import requests

                # Login to get token
                login_response = requests.post('http://localhost:8080/api/v1/auth/login',
                    json={'username': 'admin', 'password': 'admin123'})
                token = login_response.json()['token']

                # Use token for API calls
                headers = {'Authorization': f'Bearer {token}'}
                products_response = requests.get('http://localhost:8080/api/v1/products',
                    headers=headers)
                """;
    }

    private Map<String, Object> createRequestExamples() {
        return Map.of(
                "createProduct",
                        Map.of(
                                "name",
                                "iPhone 15 Pro",
                                "description",
                                "Latest Apple smartphone with A17 Pro chip",
                                "price",
                                999.99,
                                "category",
                                "ELECTRONICS",
                                "stockQuantity",
                                50,
                                "minStockLevel",
                                10,
                                "weight",
                                0.221,
                                "dimensions",
                                "159.9 × 76.7 × 8.25 mm"),
                "updateProduct",
                        Map.of(
                                "price",
                                899.99,
                                "stockQuantity",
                                75,
                                "description",
                                "Updated description with new features"));
    }

    private Map<String, Object> createResponseExamples() {
        return Map.of(
                "productResponse",
                Map.of(
                        "id", "123e4567-e89b-12d3-a456-426614174000",
                        "sku", "ELEC-001",
                        "name", "iPhone 15 Pro",
                        "description", "Latest Apple smartphone",
                        "price", 999.99,
                        "category", "ELECTRONICS",
                        "stockQuantity", 50,
                        "active", true,
                        "createdAt", "2025-08-18T10:00:00Z"));
    }

    private Map<String, Object> createErrorScenarios() {
        return Map.of(
                "productNotFound", "When requesting non-existent product ID",
                "duplicateSku", "When creating product with existing SKU",
                "validationError", "When required fields are missing or invalid",
                "unauthorizedAccess", "When accessing without proper authentication");
    }

    private Map<String, Object> createErrorExample(int status, String message) {
        return Map.of(
                "timestamp",
                "2025-08-18T10:31:36.844264303Z",
                "status",
                status,
                "error",
                getStatusText(status),
                "message",
                message,
                "path",
                "/api/v1/products");
    }

    private Map<String, Object> createValidationErrorExample() {
        return Map.of(
                "timestamp", "2025-08-18T10:31:36.844264303Z",
                "status", 422,
                "error", "Unprocessable Entity",
                "message", "Validation failed",
                "path", "/api/v1/products",
                "validationErrors",
                        List.of(
                                Map.of("field", "name", "message", "Product name is required"),
                                Map.of(
                                        "field",
                                        "price",
                                        "message",
                                        "Price must be greater than 0")));
    }

    private Map<String, Object> createWorkflowExample(String type) {
        return switch (type) {
            case "authenticate-create" -> Map.of(
                    "description",
                    "Login and create a new product",
                    "steps",
                    List.of(
                            "POST /api/v1/auth/login with credentials",
                            "Extract JWT token from response",
                            "POST /api/v1/products with Authorization header",
                            "Handle success or error response"));
            case "search-filter" -> Map.of(
                    "description",
                    "Search products with filters",
                    "steps",
                    List.of(
                            "GET /api/v1/products with query parameters",
                            "Use category, price range, or name filters",
                            "Process paginated results",
                            "Handle empty results gracefully"));
            default -> Map.of("description", "Generic workflow example");
        };
    }

    private String getStatusText(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
