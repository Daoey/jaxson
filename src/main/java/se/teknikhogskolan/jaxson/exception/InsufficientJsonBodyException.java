package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public final class InsufficientJsonBodyException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public InsufficientJsonBodyException(String message) {
        super(Response.status(BAD_REQUEST).entity(message).build());
    }
}