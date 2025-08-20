/**
 * GlobalExceptionHandler.java
 *
 * <p>Design Doc: ./docs/doc-1 - API-Design-Product-Catalog.md
 *
 * <p>Purpose: - Provides a centralized exception handling mechanism for the application.
 *
 * <p>Last Updated: 2025-08-12 by Claude (Enhanced error handling with standardized responses)
 */
package com.thedavestack.productcatalog.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.thedavestack.productcatalog.dto.ErrorResponse;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {

        log.error("Product not found: {}", ex.getMessage());

        List<ErrorResponse.HelpLink> helpLinks = createSwaggerHelpLinks();

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        ex.getMessage(),
                        getPath(request),
                        "PRODUCT_NOT_FOUND",
                        helpLinks);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateSkuException(
            DuplicateSkuException ex, WebRequest request) {

        log.error("Duplicate SKU: {}", ex.getMessage());

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.CONFLICT.value(),
                        "Conflict",
                        ex.getMessage(),
                        getPath(request),
                        "DUPLICATE_SKU");

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.error("Validation failed: {}", ex.getMessage());

        List<ErrorResponse.ValidationError> validationErrors =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(
                                error ->
                                        new ErrorResponse.ValidationError(
                                                error.getField(),
                                                error.getRejectedValue(),
                                                error.getDefaultMessage()))
                        .collect(Collectors.toList());

        ErrorResponse errorResponse =
                ErrorResponse.withValidationErrors(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation Failed",
                        "One or more fields have validation errors",
                        getPath(request),
                        validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        log.error("Constraint violation: {}", ex.getMessage());

        List<ErrorResponse.ValidationError> validationErrors =
                ex.getConstraintViolations().stream()
                        .map(
                                violation ->
                                        new ErrorResponse.ValidationError(
                                                violation.getPropertyPath().toString(),
                                                violation.getInvalidValue(),
                                                violation.getMessage()))
                        .collect(Collectors.toList());

        ErrorResponse errorResponse =
                ErrorResponse.withValidationErrors(
                        HttpStatus.BAD_REQUEST.value(),
                        "Constraint Violation",
                        "One or more constraints were violated",
                        getPath(request),
                        validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.warn("No handler found for {} {}", ex.getHttpMethod(), ex.getRequestURL());

        List<ErrorResponse.HelpLink> helpLinks = createSwaggerHelpLinks();

        String message =
                String.format(
                        "No endpoint found for %s %s. Please check the URL and HTTP method.",
                        ex.getHttpMethod(), ex.getRequestURL());

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        message,
                        getPath(request),
                        "ENDPOINT_NOT_FOUND",
                        helpLinks);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.warn("No resource found for path: {}", ex.getResourcePath());

        List<ErrorResponse.HelpLink> helpLinks = createSwaggerHelpLinks();

        String message =
                String.format(
                        "No endpoint found for the requested path '%s'. Please check the URL and HTTP method.",
                        ex.getResourcePath());

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        message,
                        getPath(request),
                        "ENDPOINT_NOT_FOUND",
                        helpLinks);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {

        log.error("Unexpected error occurred", ex);

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        "An unexpected error occurred",
                        getPath(request),
                        "INTERNAL_ERROR");

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            return servletRequest.getRequest().getRequestURI();
        }
        return request.getDescription(false);
    }

    private List<ErrorResponse.HelpLink> createSwaggerHelpLinks() {
        return List.of(
                new ErrorResponse.HelpLink("API Documentation", "/swagger-ui/index.html"),
                new ErrorResponse.HelpLink("OpenAPI Specification", "/v3/api-docs"));
    }
}
