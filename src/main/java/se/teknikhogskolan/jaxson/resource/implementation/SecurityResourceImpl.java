package se.teknikhogskolan.jaxson.resource.implementation;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.jaxson.resource.SecurityResource;
import se.teknikhogskolan.jaxson.security.JwtHelper;
import se.teknikhogskolan.springcasemanagement.security.JwtReader;
import se.teknikhogskolan.springcasemanagement.service.SecureUserService;
import se.teknikhogskolan.springcasemanagement.service.exception.NotAuthorizedException;

public final class SecurityResourceImpl implements SecurityResource {

    private final SecureUserService secureUserService;

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
        JwtHelper helper = new JwtHelper();
        Map<String, Token> tokens = new HashMap<>();
        tokens.put("authorization token", helper.generateAuthorizationToken(credentials.getUsername()));
        tokens.put("refresh token", helper.generateRefreshToken(credentials.getUsername()));
        return tokens;
    }

    @Override
    public Response authenticateUser(Credentials credentials) {
        if (secureUserService.isValid(credentials.getUsername(), credentials.getPassword())) {
            return Response.ok(createTokens(credentials)).build();
        }
        else throw new NotAuthorizedException("Password do not match username");
    }

    @Override
    public Response getNewAuthToken(String refreshToken) {
        refreshToken = refreshToken.substring(7);

        JwtReader reader =  new JwtReader();
        Map<String, String> claims = reader.readClaims(refreshToken);

        if (!"refresh".equals(claims.get("sub"))) {
            throw new BadRequestException("Only refresh Tokens is valid for retrieving authorization Tokens");
        }

        final String username = claims.get("username");
        if (null == username || username.isEmpty()) {
            throw new BadRequestException("Username is missing");
        }

        JwtHelper jwtHelper = new JwtHelper();
        return Response.ok(jwtHelper.generateAuthorizationToken(username)).build();
    }
}