package se.teknikhogskolan.jaxson.resource.implementation;

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
        String token = securityUserService.createTokenFor(credentials.getUsername(), credentials.getPassword());
        int expirationTime = getSecondsLeft(securityUserService.getExpiration(token));
        return new Token(token, expirationTime);
    }

    private int getSecondsLeft(LocalDateTime created) {
        return LocalDateTime.now().compareTo(created);
    }
}