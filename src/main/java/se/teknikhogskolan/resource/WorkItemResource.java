package se.teknikhogskolan.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

import javax.ws.rs.POST;
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

    @Autowired
    private WorkItemService workItemService;

    @POST
    public Response createWorkItem() {
        WorkItem workItem = workItemService.create("asdkfbaslkfnaksdf");
        URI location = uriInfo.getAbsolutePathBuilder().path(workItem.getId().toString()).build();
        return Response.created(location).build();
    }
}
