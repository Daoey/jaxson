package se.teknikhogskolan.jaxson.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;
import se.teknikhogskolan.springcasemanagement.service.SecureUserService;
import se.teknikhogskolan.springcasemanagement.service.exception.NotAuthorizedException;

public class AuthorizationRequestFilter implements ContainerRequestFilter {

    private final long loginDurationSecond = 60; // TODO loosen up login duration

    @Autowired
    private SecureUserService secureUserService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        UriInfo uriInfo = requestContext.getUriInfo();

        if ("register".equals(uriInfo.getPath()) || "login".equals(uriInfo.getPath())) {
            return; // Let logins and registrations pass, handled by SecurityResource
        }
        if ("token".equals(uriInfo.getPath())) {
            return; // Handled by AuthorizationRESPONSEFilter
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");
        String token = authorizationHeader.substring("Bearer".length()).trim();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        JwtReader jwtReader = new JwtReader();
        Map<String, String> claims = jwtReader.readClaims(token); // this "verifies" the token TODO verify(token)

//        secureUserService.verify(token);

//        secureUserService.renewExpiration(token); // TODO renew token in Responsefilter
    }
}