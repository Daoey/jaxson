package se.teknikhogskolan.jaxson.resource;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.IssueDto;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.jaxson.model.WorkItemsRequestBean;

@Path("workitems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkItemResource {

    @POST
    Response createWorkItem(WorkItemDto workItem);

    @GET
    Response getWorkItems(@BeanParam WorkItemsRequestBean workItemsRequestBean);
    
    @GET
    @Path("{id}")
    Response getWorkItem(@PathParam("id") Long id);

    @PUT
    @Path("{id}")
    Response updateWorkItem(@PathParam("id") Long id, WorkItemDto workItem);

    @DELETE
    @Path("{id}")
    Response deleteWorkItem(@PathParam("id") Long id);

    @DELETE
    @Path("{id}/issue")
    Response deleteAssignedIssue(@PathParam("id") Long id);
    
    @POST
    @Path("{id}/issue")
    Response createAndAssignIssue(@PathParam("id") Long id, IssueDto issue);
    
    @GET
    @Path("{id}/issue")
    Response getAssignedIssue(@PathParam("id") Long id);
    
    @PUT
    @Path("{id}/issue")
    Response updateAssignedIssue(@PathParam("id") Long id, IssueDto issue);
    
    @GET
    @Path("/issue")
    Response getAllWorkItemsWithIssue();

}
