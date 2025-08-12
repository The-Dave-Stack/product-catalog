package com.thedavestack.productcatalog.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thedavestack.productcatalog.BaseE2ETest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

@DisplayName("Invalid Endpoint E2E Tests")
public class InvalidEndpointE2ETest extends BaseE2ETest {

    private String adminToken;

    @BeforeEach
    void authenticateUser() {
        // Get admin token
        String adminLoginBody = "{ \"username\": \"admin\", \"password\": \"admin123\" }";
        Response adminResponse =
                given().contentType(ContentType.JSON)
                        .body(adminLoginBody)
                        .when()
                        .post("/api/v1/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        adminToken = "Bearer " + adminResponse.path("token");
    }

    @Test
    @DisplayName("Should return 404 with Swagger guidance for invalid API endpoint")
    void shouldReturn404WithSwaggerGuidanceForInvalidEndpoint() {
        given().header("Authorization", adminToken)
                .when()
                .get("/api/v1/invalid-endpoint")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("error", equalTo("Not Found"))
                .body("errorCode", equalTo("ENDPOINT_NOT_FOUND"))
                .body("message", containsString("No endpoint found"))
                .body("message", containsString("api/v1/invalid-endpoint"))
                .body("helpLinks", hasSize(2))
                .body("helpLinks[0].description", equalTo("API Documentation"))
                .body("helpLinks[0].url", equalTo("/swagger-ui/index.html"))
                .body("helpLinks[1].description", equalTo("OpenAPI Specification"))
                .body("helpLinks[1].url", equalTo("/v3/api-docs"));
    }

    @Test
    @DisplayName("Should return 405 for unsupported HTTP method on valid endpoint")
    void shouldReturn405ForUnsupportedHttpMethod() {
        // PATCH is not supported on /api/v1/products, only GET/POST
        // This should return 405 Method Not Allowed, not 404
        given().header("Authorization", adminToken)
                .when()
                .patch("/api/v1/products")
                .then()
                .statusCode(405); // Method Not Allowed is the correct status
    }
}
