package se.teknikhogskolan.jaxson.resource.implementation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.teknikhogskolan.jaxson.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

@Component
@Path("workitems")
public final class WorkItemResource {

    @Autowired
    WorkItemService workItemService;

    private static final String PROJECT_PACKAGE = "se.teknikhogskolan.springcasemanagement";

}
