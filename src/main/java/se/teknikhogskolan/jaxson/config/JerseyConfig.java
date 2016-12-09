package se.teknikhogskolan.jaxson.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.exception.NoSearchResultExceptionMapper;
import se.teknikhogskolan.jaxson.resource.implementation.IssueResourceImpl;
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
        register(NoSearchResultExceptionMapper.class);
    }
}