package se.teknikhogskolan.jaxson.security;

import java.io.IOException;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import se.teknikhogskolan.jaxson.exception.ErrorMessage;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;
import se.teknikhogskolan.springcasemanagement.service.exception.NotAuthorizedException;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class AuthorizationRequestFilter implements ContainerRequestFilter { // TODO CLEAN!!!1!

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // TODO check failing exception mappers
        
        UriInfo uriInfo = requestContext.getUriInfo();

        if ("register".equals(uriInfo.getPath()) || "login".equals(uriInfo.getPath())) {
            return; // Let logins and registrations pass, handled by SecurityResource
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            ErrorMessage message = new ErrorMessage(UNAUTHORIZED.getStatusCode(), UNAUTHORIZED.toString(),
                    "Authorization header must be provided");
            throw new WebApplicationException(Response.status(UNAUTHORIZED).entity(message).build());
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();
        JwtReader jwtReader = new JwtReader();
        if (!jwtReader.isValid(token)) {
            throw new WebApplicationException(Response.status(UNAUTHORIZED).entity("Broken JWT").build());
        }
        Map<String, String> claims = jwtReader.readClaims(token);
        String subject = claims.get("sub");

        if ("token".equals(uriInfo.getPath())) {
            if ("refresh".equals(subject)) return; // already verified is valid token
            else throw new BadRequestException(String.format("Only refresh tokens allowed here, this was '%s'", subject));
        } else {
            if ("refresh".equals(subject)) throw new BadRequestException("Send refresh tokens to /token");
        }

        if ("authorization".equals(subject)) return; // already verified is valid token

        throw new NotAuthorizedException("Not authorized, please login");
    }
}