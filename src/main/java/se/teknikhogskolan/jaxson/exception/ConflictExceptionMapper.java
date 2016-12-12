package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.CONFLICT;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


public class ConflictExceptionMapper  implements ExceptionMapper<ConflictException> {

    @Override
    public Response toResponse(ConflictException exception) {
        return Response.status(CONFLICT).entity(exception.getMessage()).build();
    }
}