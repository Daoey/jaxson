package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.NotFoundException;

@Provider
public final class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(NOT_FOUND)
                .entity(new ErrorMessage(NOT_FOUND.getStatusCode(), NOT_FOUND.toString(), exception.getMessage()))
                .build();
    }
}