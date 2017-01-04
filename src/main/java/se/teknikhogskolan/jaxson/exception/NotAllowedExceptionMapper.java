package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.NotAllowedException;

@Provider
public final class NotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {

    @Override
    public Response toResponse(NotAllowedException exception) {
        return Response.status(PRECONDITION_FAILED)
                .entity(new ErrorMessage(PRECONDITION_FAILED.getStatusCode(),
                        PRECONDITION_FAILED.toString(),
                        exception.getMessage()))
                .build();
    }
}