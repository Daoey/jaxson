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

    private final long loginDurationSeconds = 60; // TODO loosen up login duration and put in one place together with epoch

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        UriInfo uriInfo = requestContext.getUriInfo(); // TODO remove 'CONFIG FILE:'

        if ("token".equals(uriInfo.getPath())) { // TODO test if works

            String authorizationHeader = requestContext.getHeaderString("Authorization");
            String token = authorizationHeader.substring("Bearer".length()).trim();

            JwtReader jwtReader = new JwtReader();
            Map<String, String> claims = jwtReader.readClaims(token);

            if (!"refresh".equals(claims.get("sub"))) throw new IllegalArgumentException("Refresh token missing");

            JwtBuilder jwtBuilder = new JwtBuilder();
            jwtBuilder.putClaim("sub", "authorization");
            Long exp = getDefaultExpiration();
            jwtBuilder.putClaim("exp", exp.toString());
            jwtBuilder.putClaim("username", claims.get("username"));

            responseContext.setEntity(new Token(jwtBuilder.build(), exp.longValue()));
            return;
        }

        responseContext.getHeaders().add("X-Powered-By", "Jersey :-)");
    }

    public Long getDefaultExpiration() {
        return getNowEpochTime() + loginDurationSeconds;
    }

    public long getNowEpochTime() {
        return Instant.now().getEpochSecond();
    }
}

