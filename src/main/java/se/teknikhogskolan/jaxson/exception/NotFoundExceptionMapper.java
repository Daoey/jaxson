package se.teknikhogskolan.jaxson.exception;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.teknikhogskolan.springcasemanagement.service.exception.NotFoundException;

@Provider
public final class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        Map<String, Object> errorMessage = new HashMap();
        errorMessage.put("status", NOT_FOUND);
        errorMessage.put("code", NOT_FOUND.getStatusCode());
        errorMessage.put("message", exception.getMessage());
        if (null != exception.getMissingEntity()) {
            errorMessage.put("missing entity", exception.getMissingEntity().getSimpleName());
        }
        return Response.status(NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(errorMessage).build();
    }
}