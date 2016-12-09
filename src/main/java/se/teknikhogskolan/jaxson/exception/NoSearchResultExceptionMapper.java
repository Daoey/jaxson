package se.teknikhogskolan.jaxson.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.jaxson.model.ErrorMessage;
import se.teknikhogskolan.springcasemanagement.service.exception.NoSearchResultException;

@Provider
public final class NoSearchResultExceptionMapper implements ExceptionMapper<NoSearchResultException> {

    @Override
    public Response toResponse(NoSearchResultException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
        return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
    }

}
