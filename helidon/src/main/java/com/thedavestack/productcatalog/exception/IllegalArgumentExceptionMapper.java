package com.thedavestack.productcatalog.exception;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Context private UriInfo uriInfo;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        ErrorResponse errorResponse =
                new ErrorResponse(
                        Instant.now(),
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        "Bad Request",
                        exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : "unknown");

        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}
