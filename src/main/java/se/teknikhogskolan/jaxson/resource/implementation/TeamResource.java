package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.model.TeamModel;
import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelFrom;
import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelsFromTeams;

@Path("teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @PUT
    @Path("{id}")
    public Response addUserToTeam(@PathParam("id") Long teamId, @QueryParam("userId") Long userId) {
        teamService.addUserToTeam(teamId, userId);
        return Response.accepted().build();
    }

    @GET
    @Path("{id}/users")
    public Response getUsersInTeam(@PathParam("id") Long id, @QueryParam("asLocations") boolean asLocations) {
        List<UserModel> userModels = new ArrayList<>();
        userService.getAllByTeamId(id).forEach(user -> userModels.add(new UserModel(user)));
        if (asLocations) {
            List<String> uris = new ArrayList<>();
            userModels.forEach(u -> uris.add(String.format("../users/%d", u.getId())));
            return Response.ok(uris).build();
        }
        return Response.ok(userModels).build();
    }

    @GET
    public Response getTeam(@QueryParam("id") Long id) {
        if (weHaveA(id)) {
            Team team = teamService.getById(id);
            TeamModel teamModel = teamModelFrom(team);
            return Response.ok(teamModel).build();
        }
        Collection<TeamModel> teamModels = teamModelsFromTeams(teamService.getAll());
        return Response.ok(teamModels).build();
    }

    private boolean weHaveA(Object o) {
        return null != o;
    }

    @POST
    public Response createTeam(@QueryParam("name") String name) {
        TeamModel teamModel = teamModelFrom(teamService.create(name));
        return Response.status(Response.Status.CREATED).header("Location", "teams?id=" + teamModel.getId()).build();
    }
}
