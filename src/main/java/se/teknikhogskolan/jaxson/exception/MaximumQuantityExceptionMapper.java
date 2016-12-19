package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.MaximumQuantityException;

@Provider
public final class MaximumQuantityExceptionMapper implements ExceptionMapper<MaximumQuantityException> {

    @Override
    public Response toResponse(MaximumQuantityException exception) {
        return Response.status(FORBIDDEN).entity(exception.getMessage()).build();
    }
}