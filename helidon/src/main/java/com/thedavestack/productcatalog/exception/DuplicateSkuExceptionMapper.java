package com.thedavestack.productcatalog.exception;

import java.time.Instant;

import com.thedavestack.productcatalog.service.DuplicateSkuException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class DuplicateSkuExceptionMapper implements ExceptionMapper<DuplicateSkuException> {

    @Context private UriInfo uriInfo;

    @Override
    public Response toResponse(DuplicateSkuException exception) {
        ErrorResponse errorResponse =
                new ErrorResponse(
                        Instant.now(),
                        Response.Status.CONFLICT.getStatusCode(),
                        "Conflict",
                        exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : "unknown");

        return Response.status(Response.Status.CONFLICT).entity(errorResponse).build();
    }
}
