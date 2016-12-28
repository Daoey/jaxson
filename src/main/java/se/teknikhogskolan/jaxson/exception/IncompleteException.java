package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public final class IncompleteException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public IncompleteException(String message) {
        super(Response.status(BAD_REQUEST)
                .entity(new ErrorMessage(BAD_REQUEST.getStatusCode(), BAD_REQUEST.toString(), message))
                .build());
    }
}