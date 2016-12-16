package se.teknikhogskolan.jaxson.resource;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.model.WorkItemDto;

@Path("teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TeamResource {

    @POST
    Response createTeam(TeamDto teamDto);

    @GET
    Collection<TeamDto> getTeams();

    @GET
    @Path("{id}")
    TeamDto getTeam(@PathParam("id") Long id);

    @PUT
    Response updateTeam(TeamDto teamDto);

    @PUT
    @Path("{id}/users")
    Response addUserToTeam(@PathParam("id") Long teamId, UserDto userDto);

    @GET
    @Path("{id}/users")
    Collection<UserDto> getUsersInTeam(@PathParam("id") Long id);

    @GET
    @Path("{id}/workitems")
    Collection<WorkItemDto> getWorkItemsInTeam(@PathParam("id") Long id);
}