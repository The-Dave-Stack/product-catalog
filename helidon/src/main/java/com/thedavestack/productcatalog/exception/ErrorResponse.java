package com.thedavestack.productcatalog.exception;

import java.time.Instant;

import jakarta.json.bind.annotation.JsonbProperty;

public record ErrorResponse(
        @JsonbProperty("timestamp") Instant timestamp,
        @JsonbProperty("status") int status,
        @JsonbProperty("error") String error,
        @JsonbProperty("message") String message,
        @JsonbProperty("path") String path) {}
