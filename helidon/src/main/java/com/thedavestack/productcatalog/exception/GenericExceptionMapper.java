package com.thedavestack.productcatalog.exception;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GenericExceptionMapper.class.getName());

    @Context private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException webEx) {
            return webEx.getResponse();
        }

        LOGGER.log(Level.SEVERE, "Unexpected error occurred", exception);

        ErrorResponse errorResponse =
                new ErrorResponse(
                        Instant.now(),
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Internal Server Error",
                        "An unexpected error occurred",
                        uriInfo != null ? uriInfo.getPath() : "unknown");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }
}
