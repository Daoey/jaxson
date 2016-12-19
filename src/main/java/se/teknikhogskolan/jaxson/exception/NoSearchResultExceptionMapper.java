package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.NoSearchResultException;

@Provider
public final class NoSearchResultExceptionMapper implements ExceptionMapper<NoSearchResultException> {

    @Override
    public Response toResponse(NoSearchResultException exception) {
        return Response.status(NOT_FOUND).entity(exception.getMessage()).build();
    }
}