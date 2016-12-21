package se.teknikhogskolan.jaxson.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.exception.*;
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

        register(AuthorizationRequestFilter.class);

        register(DatabaseExceptionMapper.class);
        register(InvalidInputExceptionMapper.class);
        register(NoSearchResultExceptionMapper.class);
        register(MaximumQuantityExceptionMapper.class);
        register(IllegalArgumentExceptionMapper.class);
        register(RuntimeExceptionMapper.class);
    }
}