package se.teknikhogskolan.jaxson.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ForbiddenOperationException extends WebApplicationException {
    public ForbiddenOperationException(Response response) {
        super(response);
    }
}
