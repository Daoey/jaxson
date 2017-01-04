package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public final class PreconditionFailedException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public PreconditionFailedException(String message) {
        super(Response.status(PRECONDITION_FAILED)
                .entity(new ErrorMessage(PRECONDITION_FAILED.getStatusCode(), PRECONDITION_FAILED.toString(), message))
                .build());
    }
}