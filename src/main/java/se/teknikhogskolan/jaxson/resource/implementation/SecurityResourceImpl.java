package se.teknikhogskolan.jaxson.resource.implementation;

import java.time.Instant;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.resource.SecurityResource;
import se.teknikhogskolan.springcasemanagement.security.JwtBuilder;
import se.teknikhogskolan.springcasemanagement.service.SecureUserService;

public final class SecurityResourceImpl implements SecurityResource {

    private final SecureUserService secureUserService;

    private final long loginDurationSeconds = 60; // TODO loosen up login duration and put in one place

    @Autowired
    public SecurityResourceImpl(SecureUserService secureUserService) {
        this.secureUserService = secureUserService;
    }

    @Override
    public Response createUser(Credentials credentials) { // TODO add refresh token to response
        secureUserService.create(credentials.getUsername(), credentials.getPassword());
        return Response.ok(createToken(credentials.getUsername(), getDefaultExpiration(), "authorization")).build();
    }

    private String getDefaultExpiration() {
        return String.valueOf(getNowEpochTime() + loginDurationSeconds);
    }

    private long getNowEpochTime() {
        return Instant.now().getEpochSecond();
    }

    private String createToken(String username, String exp, String sub) {
        if (sub == null) throw new IllegalArgumentException("Subject must have value");
        if (exp == null) throw new IllegalArgumentException("Expires must have value");
        if (username == null || secureUserService.usernameIsAvailable(username)) {
            throw new IllegalArgumentException("No such user");
        }

        JwtBuilder jwtBuilder = new JwtBuilder();
        jwtBuilder.putClaim("username", username);
        jwtBuilder.putClaim("exp", exp);
        jwtBuilder.putClaim("sub", sub);

        return jwtBuilder.build();
    }

    @Override
    public Response authenticateUser(Credentials credentials) { // TODO add refresh token to response
        return Response.ok(createToken(credentials.getUsername(), getDefaultExpiration(), "authorization")).build();
    }

    @Override
    public Response getNewAuthToken() {
        return null;
    }

}