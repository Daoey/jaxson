package se.teknikhogskolan.jaxson.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.model.WorkItemModel;
import se.teknikhogskolan.jaxson.resource.implementation.IssueResource;
import se.teknikhogskolan.jaxson.resource.implementation.TeamResource;
import se.teknikhogskolan.jaxson.resource.implementation.UserResourceImpl;
import se.teknikhogskolan.jaxson.resource.implementation.WorkItemResourceImpl;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("jaxson")
public final class JerseyConfig extends ResourceConfig{

    public JerseyConfig() {
        register(TeamResource.class);
        register(IssueResource.class);
        register(UserResourceImpl.class);
        register(WorkItemResourceImpl.class);
    }
}