package se.teknikhogskolan.jaxson.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.WorkItemModel;

@Path("workitems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkItemResource {

    @POST
    Response createWorkItem(WorkItemModel workItem);

    // TODO change to beanparam
    @GET
    Response getWorkItems(@QueryParam("page") int page, @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("description") String description, @QueryParam("userNumber") Long userNumber,
            @QueryParam("status") String status, @QueryParam("createdAfter") String createdAfter,
            @QueryParam("createdBefore") String createdBefore, @QueryParam("teamId") Long teamId,
            @QueryParam("hasIssue") Boolean hasIssue, @QueryParam("completedAfter") String completedAfter,
            @QueryParam("completedBefore") String completedBefore);
    
    @GET
    @Path("{id}")
    Response getWorkItem(@PathParam("id") Long id);

    @PUT
    @Path("{id}")
    Response updateWorkItem(@PathParam("id") Long id, WorkItemModel workItem);

    @DELETE
    @Path("{id}")
    Response deleteWorkItem(@PathParam("id") Long id,
            @QueryParam("deleteOnlyAssignedIssue") Boolean deleteOnlyAssignedIssue);

    @DELETE
    @Path("{id}/issues")
    Response deleteAssignedIssue(@PathParam("id") Long id);

}
