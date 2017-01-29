package se.teknikhogskolan.jaxson.security;

import java.io.IOException;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import se.teknikhogskolan.jaxson.exception.ErrorMessage;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        final String requestedPath = requestContext.getUriInfo().getPath();
        final String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (isResource(requestedPath) && isAuthorized(authorizationHeader)) {
            return;
        }

        if (isAuthToken(requestedPath) && hasValidRefreshToken(authorizationHeader)) {
            return;
        }

        if (isLogin(requestedPath)) {
            return;
        }

        final ErrorMessage message = new ErrorMessage(UNAUTHORIZED.getStatusCode(), UNAUTHORIZED.toString(),
                "Not authorized, please login");
        throw new WebApplicationException(Response.status(UNAUTHORIZED).entity(message).build());
    }

    private boolean isAuthToken(String path) {
        return "token".equals(path);
    }

    private boolean isAuthorized(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) return false;
        String token = authorizationHeader.substring("Bearer".length()).trim();
        JwtReader jwtReader = new JwtReader();
        return jwtReader.isValid(token);
    }

    private boolean hasValidRefreshToken(String authorizationHeader) {
        if (!isAuthorized(authorizationHeader)) return false;

        String token = authorizationHeader.substring("Bearer".length()).trim();
        JwtReader jwtReader = new JwtReader();
        Map<String, String> claims = jwtReader.readClaims(token);

        return "refresh".equals(claims.get("sub"));
    }

    private boolean isLogin(String resource) {
        return "login".equals(resource);
    }

    private boolean isResource(String resource) {
        return "users".equals(resource) || "workitems".equals(resource) || "teams".equals(resource);
    }
}