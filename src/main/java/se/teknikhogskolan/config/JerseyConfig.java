package se.teknikhogskolan.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import se.teknikhogskolan.model.WorkItem;
import se.teknikhogskolan.resource.IssueResource;
import se.teknikhogskolan.resource.TeamResource;
import se.teknikhogskolan.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

import javax.ws.rs.ApplicationPath;

@Component
public final class JerseyConfig extends ResourceConfig{

    public JerseyConfig() {
        register(TeamResource.class);
        register(WorkItem.class);
        register(IssueResource.class);
        register(UserResource.class);
        register(WorkItemService.class);
    }
}