package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import se.teknikhogskolan.springcasemanagement.service.exception.InvalidInputException;

public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {

    @Override
    public Response toResponse(InvalidInputException exception) {
        return Response.status(FORBIDDEN).entity(exception.getMessage()).build();
    }
}