package se.teknikhogskolan.jaxson.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.resource.implementation.IssueResource;
import se.teknikhogskolan.jaxson.resource.implementation.TeamResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.UserResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.WorkItemResourceImpl;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("jaxson")
public final class JerseyConfig extends ResourceConfig{

    public JerseyConfig() {
        register(TeamResourceImpl.class);
        register(IssueResource.class);
        register(UserResourceImpl.class);
        register(WorkItemResourceImpl.class);
    }
}