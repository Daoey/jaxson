package se.teknikhogskolan.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import se.teknikhogskolan.model.WorkItem;
import se.teknikhogskolan.resource.IssueResource;
import se.teknikhogskolan.resource.TeamResource;
import se.teknikhogskolan.resource.UserResource;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("jaxson")
public final class JerseyConfig extends ResourceConfig{

    public JerseyConfig() {
        register(TeamResource.class);
        register(WorkItem.class);
        register(IssueResource.class);
        register(UserResource.class);
    }
}