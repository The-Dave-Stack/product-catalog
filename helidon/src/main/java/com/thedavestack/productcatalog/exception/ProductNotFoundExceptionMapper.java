package com.thedavestack.productcatalog.exception;

import java.time.Instant;

import com.thedavestack.productcatalog.service.ProductNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ProductNotFoundExceptionMapper implements ExceptionMapper<ProductNotFoundException> {

    @Context private UriInfo uriInfo;

    @Override
    public Response toResponse(ProductNotFoundException exception) {
        ErrorResponse errorResponse =
                new ErrorResponse(
                        Instant.now(),
                        Response.Status.NOT_FOUND.getStatusCode(),
                        "Not Found",
                        exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : "unknown");

        return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
    }
}
