package se.teknikhogskolan.jaxson.resource.implementation;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.jaxson.resource.SecurityResource;
import se.teknikhogskolan.springcasemanagement.security.JwtBuilder;
import se.teknikhogskolan.springcasemanagement.service.SecureUserService;

public final class SecurityResourceImpl implements SecurityResource {

    private final SecureUserService secureUserService;

    private final long loginDurationSeconds = 60; // TODO loosen up login duration and put in one place
    private final long refreshDurationSeconds = 7 *24 * 60 * 60;

    @Autowired
    public SecurityResourceImpl(SecureUserService secureUserService) {
        this.secureUserService = secureUserService;
    }

    @Override
    public Response createUser(Credentials credentials) {
        secureUserService.create(credentials.getUsername(), credentials.getPassword());
        return Response.ok(createTokens(credentials)).build();
    }

    private Map<String, Token> createTokens(Credentials credentials) {
        Map<String, Token> tokens = new HashMap<>();

        Long authorizationExpiration = getDefaultExpiration();
        Token authToken = new Token(createJwt(credentials.getUsername(), authorizationExpiration.toString(), "authorization"), authorizationExpiration);
        tokens.put("authorization token", authToken);

        Long refreshExpiration = getRefreshExpiration();
        Token refreshToken = new Token(createJwt(credentials.getUsername(), refreshExpiration.toString(), "refresh"), refreshExpiration);
        tokens.put("refresh token", refreshToken);

        return tokens;
    }

    private Long getDefaultExpiration() {
        return (getNowEpochTime() + loginDurationSeconds);
    }

    private long getNowEpochTime() {
        return Instant.now().getEpochSecond();
    }

    private Long getRefreshExpiration() {
        return (getNowEpochTime() + refreshDurationSeconds);
    }

    private String createJwt(String username, String exp, String sub) {
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
    public Response authenticateUser(Credentials credentials) {
        return Response.ok(createTokens(credentials)).build();
    }

    @Override
    public Response getNewAuthToken() {
        return null;
    }

}