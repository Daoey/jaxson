package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.NotAuthorizedException;

@Provider
public final class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    @Override
    public Response toResponse(NotAuthorizedException exception) {
        return Response.status(UNAUTHORIZED).entity(new ErrorMessage(
                UNAUTHORIZED.getStatusCode(),
                UNAUTHORIZED.toString(),
                "The username and password did not match")).build();
    }
}