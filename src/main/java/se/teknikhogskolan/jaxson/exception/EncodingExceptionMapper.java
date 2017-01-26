package se.teknikhogskolan.jaxson.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import se.teknikhogskolan.springcasemanagement.security.exception.EncodingException;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public final class EncodingExceptionMapper implements ExceptionMapper<EncodingException> {

    @Override
    public Response toResponse(EncodingException e) {
        return Response.status(BAD_REQUEST).entity(new ErrorMessage(
                BAD_REQUEST.getStatusCode(),
                BAD_REQUEST.toString(),
                e.getMessage())).build();
    }
}