package se.teknikhogskolan.jaxson.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.teknikhogskolan.jaxson.model.ErrorMessage;

public final class InsufficientJsonBodyException extends WebApplicationException {

    private static final long serialVersionUID = 8921460702643087166L;

    public InsufficientJsonBodyException(String message) {
        super(Response.status(Status.BAD_REQUEST).entity(new ErrorMessage(message)).build());
    }

}
