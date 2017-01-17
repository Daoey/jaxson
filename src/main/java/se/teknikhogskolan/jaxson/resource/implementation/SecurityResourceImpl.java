package se.teknikhogskolan.jaxson.resource.implementation;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.resource.SecurityResource;
import se.teknikhogskolan.springcasemanagement.model.SecurityUser;
import se.teknikhogskolan.springcasemanagement.service.SecurityUserService;

public final class SecurityResourceImpl implements SecurityResource {

    private final SecurityUserService securityUserService;

    @Autowired
    public SecurityResourceImpl(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Override
    public Response createUser(Credentials credentials) {
        if (noNullValues(credentials)) {
            //securityUserService.create(credentials.getUsername(), credentials.getPassword());
            Optional<SecurityUser> securityUser = null;
            if (securityUser.isPresent()) {
                /*securityUserService.createSecurityUserToken(securityUser.get().getId(),
                        LocalDate.now(), LocalDateTime.now().plusMinutes(30));*/
                String token = "Token";
                return Response.ok(token).build();
            }
        }
        throw new IllegalArgumentException("Could not create security user without"
                + " JSON body containing username and password.");
    }

    private boolean noNullValues(Credentials credentials) {
        return credentials.getUsername() != null
                && credentials.getPassword() != null;
    }

    @Override
    public Response authenticateUser(Credentials credentials) {
        // securityUserService.get(credentials.getUsername(), credentials.getPassword());
        Optional<SecurityUser> securityUser = null;
        if (securityUser.isPresent()) {
            /*securityUserService.createSecurityUserToken(securityUser.get().getId(),
                    LocalDate.now(), LocalDateTime.now().plusMinutes(30));*/
            String token = "Token";
            return Response.ok(token).build();
        }
        throw new IllegalArgumentException("");
    }

    @Override
    public Response refreshToken(String token) {
        String newToken = "New Token"; //securityUserService.updateToken(token);
        return Response.ok(newToken).build();
    }
}