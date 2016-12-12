package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.CONFLICT;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ForbiddenOperationException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public ForbiddenOperationException(String message) {
        super(Response.status(CONFLICT).entity(message).build());
    }
}