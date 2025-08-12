package com.thedavestack.productcatalog.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thedavestack.productcatalog.BaseE2ETest;

import io.restassured.http.ContentType;

@DisplayName("Authentication Error E2E Tests")
public class AuthenticationErrorE2ETest extends BaseE2ETest {

    @Test
    @DisplayName("Should return 401 with Swagger guidance for invalid login credentials")
    void shouldReturn401WithSwaggerGuidanceForInvalidCredentials() {
        String invalidLoginBody = "{ \"username\": \"invalid\", \"password\": \"invalid\" }";

        given().contentType(ContentType.JSON)
                .body(invalidLoginBody)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", equalTo("Unauthorized"))
                .body("errorCode", equalTo("INVALID_CREDENTIALS"))
                .body("message", containsString("Invalid username or password"))
                .body("message", containsString("admin/admin123"))
                .body("message", containsString("user/user123"))
                .body("path", equalTo("/api/v1/auth/login"))
                .body("helpLinks", hasSize(3))
                .body("helpLinks[0].description", equalTo("Authentication Guide"))
                .body("helpLinks[0].url", equalTo("/swagger-ui/index.html#/Authentication"))
                .body("helpLinks[1].description", equalTo("API Documentation"))
                .body("helpLinks[1].url", equalTo("/swagger-ui/index.html"))
                .body("helpLinks[2].description", equalTo("OpenAPI Specification"))
                .body("helpLinks[2].url", equalTo("/v3/api-docs"));
    }

    @Test
    @DisplayName("Should return 401 with Swagger guidance for missing Authorization header")
    void shouldReturn401WithSwaggerGuidanceForMissingAuthHeader() {
        given().when()
                .get("/api/v1/products")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", equalTo("Unauthorized"))
                .body("errorCode", equalTo("AUTHENTICATION_REQUIRED"))
                .body("message", containsString("Authentication required to access this resource"))
                .body("message", containsString("valid JWT token"))
                .body("message", containsString("Authorization header"))
                .body("path", equalTo("/api/v1/products"))
                .body("helpLinks", hasSize(3))
                .body("helpLinks[0].description", equalTo("Login Endpoint"))
                .body("helpLinks[0].url", equalTo("/swagger-ui/index.html#/Authentication"))
                .body("helpLinks[1].description", equalTo("API Documentation"))
                .body("helpLinks[1].url", equalTo("/swagger-ui/index.html"))
                .body("helpLinks[2].description", equalTo("OpenAPI Specification"))
                .body("helpLinks[2].url", equalTo("/v3/api-docs"));
    }

    @Test
    @DisplayName("Should return 401 with Swagger guidance for invalid JWT token")
    void shouldReturn401WithSwaggerGuidanceForInvalidJwtToken() {
        given().header("Authorization", "Bearer invalid-jwt-token")
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", equalTo("Unauthorized"))
                .body("errorCode", equalTo("AUTHENTICATION_REQUIRED"))
                .body("message", containsString("Authentication required to access this resource"))
                .body("message", containsString("valid JWT token"))
                .body("path", equalTo("/api/v1/products"))
                .body("helpLinks", hasSize(3))
                .body("helpLinks[0].description", equalTo("Login Endpoint"))
                .body("helpLinks[0].url", equalTo("/swagger-ui/index.html#/Authentication"))
                .body("helpLinks[1].description", equalTo("API Documentation"))
                .body("helpLinks[1].url", equalTo("/swagger-ui/index.html"))
                .body("helpLinks[2].description", equalTo("OpenAPI Specification"))
                .body("helpLinks[2].url", equalTo("/v3/api-docs"));
    }

    @Test
    @DisplayName("Should return 401 with Swagger guidance for malformed Authorization header")
    void shouldReturn401WithSwaggerGuidanceForMalformedAuthHeader() {
        given().header("Authorization", "InvalidFormat jwt-token-here")
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", equalTo("Unauthorized"))
                .body("errorCode", equalTo("AUTHENTICATION_REQUIRED"))
                .body("message", containsString("Authentication required to access this resource"))
                .body("path", equalTo("/api/v1/products"))
                .body("helpLinks", hasSize(3))
                .body("helpLinks[0].description", equalTo("Login Endpoint"))
                .body("helpLinks[0].url", equalTo("/swagger-ui/index.html#/Authentication"))
                .body("helpLinks[1].description", equalTo("API Documentation"))
                .body("helpLinks[1].url", equalTo("/swagger-ui/index.html"))
                .body("helpLinks[2].description", equalTo("OpenAPI Specification"))
                .body("helpLinks[2].url", equalTo("/v3/api-docs"));
    }

    @Test
    @DisplayName(
            "Should return 401 with Swagger guidance when accessing protected POST endpoint without auth")
    void shouldReturn401WithSwaggerGuidanceForProtectedPostEndpoint() {
        String productBody =
                "{ \"name\": \"Test Product\", \"price\": 99.99, \"category\": \"ELECTRONICS\", \"stockQuantity\": 10 }";

        given().contentType(ContentType.JSON)
                .body(productBody)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", equalTo("Unauthorized"))
                .body("errorCode", equalTo("AUTHENTICATION_REQUIRED"))
                .body("message", containsString("Authentication required to access this resource"))
                .body("path", equalTo("/api/v1/products"))
                .body("helpLinks", hasSize(3))
                .body("helpLinks[0].description", equalTo("Login Endpoint"))
                .body("helpLinks[0].url", equalTo("/swagger-ui/index.html#/Authentication"))
                .body("helpLinks[1].description", equalTo("API Documentation"))
                .body("helpLinks[1].url", equalTo("/swagger-ui/index.html"))
                .body("helpLinks[2].description", equalTo("OpenAPI Specification"))
                .body("helpLinks[2].url", equalTo("/v3/api-docs"));
    }
}
