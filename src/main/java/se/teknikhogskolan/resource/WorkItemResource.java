package se.teknikhogskolan.resource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.repository.IssueRepository;
import se.teknikhogskolan.springcasemanagement.repository.UserRepository;
import se.teknikhogskolan.springcasemanagement.repository.WorkItemRepository;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Component
@Path("workitems")
public final class WorkItemResource {

    @Context
    private UriInfo uriInfo;

    private static final String PROJECT_PACKAGE = "se.teknikhogskolan.springcasemanagement";

    @GET
    public Response createWorkItem() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.scan(PROJECT_PACKAGE);
            context.refresh();

            WorkItemRepository workItemRepository = context.getBean(WorkItemRepository.class);
            UserRepository userRepository = context.getBean(UserRepository.class);
            IssueRepository issueRepository = context.getBean(IssueRepository.class);
            WorkItemService workItemService = new WorkItemService(workItemRepository, userRepository, issueRepository);

            WorkItem workItem = workItemService.create("asdkfbaslkfnaksdf");
            URI location = uriInfo.getAbsolutePathBuilder().path(workItem.getId().toString()).build();
            return Response.ok("asdgasdgsdg").build();
        }
    }
}
