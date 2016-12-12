package se.teknikhogskolan.jaxson.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.TeamDto;

@Path("teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TeamResource {

    @PUT
    @Path("{id}")
    Response addUserToTeam(@PathParam("id") Long teamId, @QueryParam("userId") Long userId);

    @POST
    Response createTeam(TeamDto teamDto); // TODO take JSON not QueryParam

    @GET
    Response getTeam(@QueryParam("id") Long id);

    @GET
    @Path("{id}/users")
    Response getUsersInTeam(@PathParam("id") Long id, @QueryParam("asLocations") boolean asLocations);

    @PUT
    Response updateTeam(TeamDto teamDto); // TODO take JSON
}
