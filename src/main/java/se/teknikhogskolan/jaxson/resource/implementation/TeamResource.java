package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.model.TeamModel;
import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelFrom;
import static se.teknikhogskolan.jaxson.model.ModelParser.userModelsFromUsers;

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

    @GET
    @Path("{id}/users")
    public Response getUsersInTeam(@PathParam("id") Long id, @QueryParam("asLocations") boolean asLocations) {
        List<UserModel> userModels = userModelsFromUsers(userService.getAllByTeamId(id));
        if (asLocations) {
            List<URI> uris = new ArrayList<>();
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            userModels.forEach(u -> {
                uris.add(uriBuilder.path(u.getId().toString()).build());
            });
            return Response.ok(uris.toString()).build();
        }
        return Response.ok(userModels).build();
    }

    @GET
    public Response getTeam(@QueryParam("id") Long id) {
        Team team = teamService.getById(id);
        TeamModel teamModel = teamModelFrom(team);
        return Response.ok(teamModel).build();
    }

    @POST
    public Response createTeam(@QueryParam("name") String name) {
        TeamModel teamModel = teamModelFrom(teamService.create(name));
        return Response.status(Response.Status.CREATED).header("Location", "teams?id=" + teamModel.getId()).build();
    }
}
