package se.teknikhogskolan.jaxson.security;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.exception.ErrorMessage;
import se.teknikhogskolan.springcasemanagement.service.SecurityUserService;

public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Autowired
    private SecurityUserService securityUserService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Return if security resource
        UriInfo uriInfo = requestContext.getUriInfo();
        if ("register".equals(uriInfo.getPath()) || "login".equals(uriInfo.getPath())) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorMessage(UNAUTHORIZED.getStatusCode(),
                            UNAUTHORIZED.toString(), "Authorization header must be provided")).build());
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorMessage(UNAUTHORIZED.getStatusCode(), UNAUTHORIZED.toString(), "Not authorized"))
                    .build());
        }
    }

    private void validateToken(String token) throws Exception {
        // Check if it was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        // securityUserService.validate(token);
    }
}