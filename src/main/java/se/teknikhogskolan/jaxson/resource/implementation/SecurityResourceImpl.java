package se.teknikhogskolan.jaxson.resource.implementation;

import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.jaxson.resource.SecurityResource;
import se.teknikhogskolan.springcasemanagement.model.SecurityUser;
import se.teknikhogskolan.springcasemanagement.service.SecurityUserService;
import se.teknikhogskolan.springcasemanagement.service.exception.DatabaseException;

public final class SecurityResourceImpl implements SecurityResource {

    private final SecurityUserService securityUserService;

    @Autowired
    public SecurityResourceImpl(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Override
    public Response createUser(Credentials credentials) {
        SecurityUser securityUser = securityUserService.create(credentials.getUsername(), credentials.getPassword());
        Token token = getTokenFromSecurityUser(securityUser);
        return Response.ok(token).build();
    }

    private Token getTokenFromSecurityUser(SecurityUser securityUser) {
        Iterator entries = securityUser.getTokensExpiration().entrySet().iterator();
        if (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            return new Token(thisEntry.getKey().toString(), thisEntry.getValue().toString());
        }
        throw new DatabaseException();
    }

    @Override
    public Response authenticateUser(Credentials credentials) {
        String token = securityUserService.createTokenFor(credentials.getUsername(), credentials.getPassword());
        return Response.ok(token).build();
    }
}