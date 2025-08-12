package com.thedavestack.productcatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thedavestack.productcatalog.dto.AuthResponse;
import com.thedavestack.productcatalog.dto.ErrorResponse;
import com.thedavestack.productcatalog.dto.LoginRequest;
import com.thedavestack.productcatalog.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final JwtUtil jwtUtil;

    @Value("${app.jwt.expiration:86400}")
    private int jwtExpirationInSeconds;

    @Operation(
            summary = "User login",
            description =
                    "Authenticate user and return JWT token. Use 'admin/admin123' for admin access or 'user/user123' for user access.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Login successful",
                        content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid credentials",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        // Simple hardcoded authentication for demo purposes
        // In production, this would authenticate against a database
        String role = authenticateUser(loginRequest.username(), loginRequest.password());

        if (role == null) {
            List<ErrorResponse.HelpLink> helpLinks = createLoginHelpLinks();

            String message =
                    "Invalid username or password. Use 'admin/admin123' for admin access or 'user/user123' for user access.";

            ErrorResponse errorResponse =
                    ErrorResponse.of(
                            401,
                            "Unauthorized",
                            message,
                            "/api/v1/auth/login",
                            "INVALID_CREDENTIALS",
                            helpLinks);
            return ResponseEntity.status(401).body(errorResponse);
        }

        String token = jwtUtil.generateToken(loginRequest.username(), role);

        AuthResponse authResponse =
                AuthResponse.of(token, loginRequest.username(), role, jwtExpirationInSeconds);

        return ResponseEntity.ok(authResponse);
    }

    private String authenticateUser(String username, String password) {
        // Hardcoded users for demo purposes
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "ADMIN";
        } else if ("user".equals(username) && "user123".equals(password)) {
            return "USER";
        }
        return null;
    }

    private List<ErrorResponse.HelpLink> createLoginHelpLinks() {
        return List.of(
                new ErrorResponse.HelpLink(
                        "Authentication Guide", "/swagger-ui/index.html#/Authentication"),
                new ErrorResponse.HelpLink("API Documentation", "/swagger-ui/index.html"),
                new ErrorResponse.HelpLink("OpenAPI Specification", "/v3/api-docs"));
    }
}
