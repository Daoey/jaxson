package se.teknikhogskolan.jaxson.security;

import java.io.IOException;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;
import se.teknikhogskolan.springcasemanagement.service.exception.NotAuthorizedException;

public class AuthorizationRequestFilter implements ContainerRequestFilter { // TODO CLEAN!!!1!

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        UriInfo uriInfo = requestContext.getUriInfo();

        if ("register".equals(uriInfo.getPath()) || "login".equals(uriInfo.getPath())) {
            return; // Let logins and registrations pass, handled by SecurityResource
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");
        String token = authorizationHeader.substring("Bearer".length()).trim();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        JwtReader jwtReader = new JwtReader();
        Map<String, String> claims = jwtReader.readClaims(token); // this "verifies" the token TODO verify(token)
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