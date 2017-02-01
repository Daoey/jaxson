package se.teknikhogskolan.jaxson.security;

import java.io.IOException;
import java.util.Map;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;

public class AuthorizationResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorized(authorizationHeader) && shouldHaveNewToken(requestContext.getUriInfo())) {
            Map<String, String> claims = getClaims(authorizationHeader);

            JwtHelper jwtHelper = new JwtHelper();
            Token token = jwtHelper.generateAccessToken(claims.get("username"));

            responseContext.getHeaders().add("access-token", token.getToken());
            responseContext.getHeaders().add("access-token-expires", token.getExpirationTime());
        }
    }

    private boolean authorized(String authorizationHeader) {
        if (null == authorizationHeader) return false;
        final String token = authorizationHeader.substring("Bearer".length()).trim();
        final JwtReader jwtReader = new JwtReader();
        return jwtReader.isValid(token);
    }

    private boolean shouldHaveNewToken(UriInfo uriInfo) {
        return "users".equals(uriInfo.getPath()) || "teams".equals(uriInfo.getPath()) || "workitems".equals(uriInfo.getPath());
    }

    private Map<String, String> getClaims(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer".length()).trim();
        JwtReader jwtReader = new JwtReader();
        return jwtReader.readClaims(token);
    }
}

