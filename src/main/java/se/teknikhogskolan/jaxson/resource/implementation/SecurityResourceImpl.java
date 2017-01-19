package se.teknikhogskolan.jaxson.resource.implementation;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDateTime;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.jaxson.resource.SecurityResource;
import se.teknikhogskolan.springcasemanagement.service.SecurityUserService;

public final class SecurityResourceImpl implements SecurityResource {

    private final SecurityUserService securityUserService;

    @Autowired
    public SecurityResourceImpl(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Override
    public Response createUser(Credentials credentials) {
        securityUserService.create(credentials.getUsername(), credentials.getPassword());
        return Response.ok(createTokenWithCredentials(credentials)).build();
    }

    @Override
    public Response authenticateUser(Credentials credentials) {
        return Response.ok(createTokenWithCredentials(credentials)).build();
    }

    private Token createTokenWithCredentials(Credentials credentials) {
        if (credentials.getPassword() == null) {
            throw new IllegalArgumentException("Password can't be null");
        }
        if (credentials.getUsername() == null) {
            throw new IllegalArgumentException("Username can't be null");
        }
        String token = securityUserService.createTokenFor(credentials.getUsername(), credentials.getPassword());
        long expirationTime = getSecondsLeft(securityUserService.getExpiration(token));
        return new Token(token, expirationTime);
    }

    private long getSecondsLeft(LocalDateTime created) {
        return SECONDS.between(LocalDateTime.now(), created);
    }
}