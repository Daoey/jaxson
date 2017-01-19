package se.teknikhogskolan.jaxson.security;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.springcasemanagement.service.SecurityUserService;
import se.teknikhogskolan.springcasemanagement.service.exception.NotAuthorizedException;

public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Autowired
    private SecurityUserService securityUserService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        // Return if security resource
        UriInfo uriInfo = requestContext.getUriInfo();
        if ("register".equals(uriInfo.getPath()) || "login".equals(uriInfo.getPath())) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        securityUserService.verify(token);
    }

}