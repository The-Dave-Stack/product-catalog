package com.thedavestack.productcatalog.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thedavestack.productcatalog.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        log.error("Unauthorized access attempt: {}", authException.getMessage());

        List<ErrorResponse.HelpLink> helpLinks = createAuthenticationHelpLinks();

        String message =
                "Authentication required to access this resource. Please provide a valid JWT token in the Authorization header.";

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized",
                        message,
                        request.getRequestURI(),
                        "AUTHENTICATION_REQUIRED",
                        helpLinks);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private List<ErrorResponse.HelpLink> createAuthenticationHelpLinks() {
        return List.of(
                new ErrorResponse.HelpLink(
                        "Login Endpoint", "/swagger-ui/index.html#/Authentication"),
                new ErrorResponse.HelpLink("API Documentation", "/swagger-ui/index.html"),
                new ErrorResponse.HelpLink("OpenAPI Specification", "/v3/api-docs"));
    }
}
