package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.CONFLICT;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ForbiddenOperationException extends WebApplicationException {
    public ForbiddenOperationException(String message) {
        super(Response.status(CONFLICT).entity(message).build());
    }
}
