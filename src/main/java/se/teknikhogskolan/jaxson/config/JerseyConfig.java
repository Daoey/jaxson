package se.teknikhogskolan.jaxson.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.exception.DatabaseExceptionMapper;
import se.teknikhogskolan.jaxson.exception.IllegalArgumentExceptionMapper;
import se.teknikhogskolan.jaxson.exception.MaximumQuantityExceptionMapper;
import se.teknikhogskolan.jaxson.exception.NotAllowedExceptionMapper;
import se.teknikhogskolan.jaxson.exception.NotFoundExceptionMapper;
import se.teknikhogskolan.jaxson.exception.RuntimeExceptionMapper;
import se.teknikhogskolan.jaxson.resource.implementation.SecurityResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.TeamResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.UserResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.WorkItemResourceImpl;
import se.teknikhogskolan.jaxson.security.AuthorizationRequestFilter;

@Component
@ApplicationPath("jaxson")
public final class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TeamResourceImpl.class);
        register(UserResourceImpl.class);
        register(WorkItemResourceImpl.class);
        register(SecurityResourceImpl.class);

        register(AuthorizationRequestFilter.class);

        register(DatabaseExceptionMapper.class);
        register(NotFoundExceptionMapper.class);
        register(MaximumQuantityExceptionMapper.class);
        register(NotAllowedExceptionMapper.class);
        register(IllegalArgumentExceptionMapper.class);
        register(RuntimeExceptionMapper.class);
    }
}