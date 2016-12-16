package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.DatabaseException;

@Provider
public final class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

    @Override
    public Response toResponse(DatabaseException exception) {
        return Response.status(INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
    }
}