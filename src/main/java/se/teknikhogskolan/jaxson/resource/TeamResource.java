package se.teknikhogskolan.jaxson.resource;

import java.util.Collection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.UserDto;

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
    Response getUsersInTeam(@PathParam("id") Long id, @QueryParam("asLocations") boolean asLocations);
}