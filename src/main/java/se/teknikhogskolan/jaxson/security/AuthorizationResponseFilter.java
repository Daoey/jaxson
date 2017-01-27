package se.teknikhogskolan.jaxson.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.springcasemanagement.security.JwtBuilder;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;

public class AuthorizationResponseFilter implements ContainerResponseFilter {

    // TODO put login duration and refresh time in config file
    private final long loginDurationSeconds = 60; // TODO loosen up login duration and put in one place together with epoch

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        UriInfo uriInfo = requestContext.getUriInfo();

        if ("token".equals(uriInfo.getPath())) { // TODO move to resource
            // TODO https://www.mkyong.com/webservices/jax-rs/get-http-header-in-jax-rs/

            Map<String, String> claims = getClaims(requestContext);

            if (!"refresh".equals(claims.get("sub"))) throw new IllegalArgumentException("Refresh token missing");

            responseContext.setEntity(createAuthToken(claims.get("username")));

        }

        if ("users".equals(uriInfo.getPath()) || "teams".equals(uriInfo.getPath()) || "workitems".equals(uriInfo.getPath())) {
            Map<String, String> claims = getClaims(requestContext);

            Token token = createAuthToken(claims.get("username"));

            responseContext.getHeaders().add("authorization-token", token.getToken());
            responseContext.getHeaders().add("authorization-token-expires", token.getExpirationTime());
        }
    }

    private Map<String, String> getClaims(ContainerRequestContext requestContext) {

        String authorizationHeader = requestContext.getHeaderString("Authorization");
        String token = authorizationHeader.substring("Bearer".length()).trim();

        JwtReader jwtReader = new JwtReader();

        return jwtReader.readClaims(token);
    }

    private Token createAuthToken(String username) {

        JwtBuilder jwtBuilder = new JwtBuilder();
        jwtBuilder.putClaim("sub", "authorization");
        Long exp = getDefaultExpiration();
        jwtBuilder.putClaim("exp", exp.toString());
        jwtBuilder.putClaim("username", username);

        return new Token(jwtBuilder.build(), exp.longValue());

    }

    public Long getDefaultExpiration() {
        return getNowEpochTime() + loginDurationSeconds;
    }

    public long getNowEpochTime() {
        return Instant.now().getEpochSecond();
    }
}

