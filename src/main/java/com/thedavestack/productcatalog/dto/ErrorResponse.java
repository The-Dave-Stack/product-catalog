package com.thedavestack.productcatalog.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String errorCode,
        List<ValidationError> validationErrors) {
    
    public record ValidationError(String field, Object rejectedValue, String message) {}
    
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path, null, null);
    }
    
    public static ErrorResponse of(int status, String error, String message, String path, String errorCode) {
        return new ErrorResponse(Instant.now(), status, error, message, path, errorCode, null);
    }
    
    public static ErrorResponse withValidationErrors(int status, String error, String message, String path, List<ValidationError> validationErrors) {
        return new ErrorResponse(Instant.now(), status, error, message, path, "VALIDATION_FAILED", validationErrors);
    }
}