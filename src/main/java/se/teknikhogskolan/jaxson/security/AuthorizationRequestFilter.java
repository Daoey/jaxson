package se.teknikhogskolan.jaxson.security;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.exception.ErrorMessage;

public class AuthorizationRequestFilter implements ContainerRequestFilter {

    //TODO User tokens?!

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String userAuthorizationCode = requestContext.getHeaderString("Authorization");

        if (!"Basic cm9vdDpzZWNyZXQ=".equals(userAuthorizationCode)) {
            requestContext.abortWith(Response
                    .status(UNAUTHORIZED)
                    .entity(new ErrorMessage(UNAUTHORIZED.getStatusCode(), UNAUTHORIZED.toString(),
                            "Missing key in request"))
                    .build());
        }
    }
}