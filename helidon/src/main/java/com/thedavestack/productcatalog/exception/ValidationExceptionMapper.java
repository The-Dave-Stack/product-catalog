package com.thedavestack.productcatalog.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context private UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message =
                exception.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));

        ErrorResponse errorResponse =
                new ErrorResponse(
                        Instant.now(),
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        "Validation Error",
                        message.isEmpty() ? "Validation failed" : message,
                        uriInfo != null ? uriInfo.getPath() : "unknown");

        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}
