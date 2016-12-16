package se.teknikhogskolan.jaxson.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.exception.*;
import se.teknikhogskolan.jaxson.resource.implementation.TeamResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.UserResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.WorkItemResourceImpl;

@Component
@ApplicationPath("jaxson")
public final class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TeamResourceImpl.class);
        register(UserResourceImpl.class);
        register(WorkItemResourceImpl.class);
        register(DatabaseExceptionMapper.class);
        register(InvalidInputExceptionMapper.class);
        register(NoSearchResultExceptionMapper.class);
        register(IllegalArgumentExceptionMapper.class);
        register(MaximumQuantityExceptionMapper.class);
        register(RuntimeExceptionMapper.class);
    }
}