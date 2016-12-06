package se.teknikhogskolan.resource;

import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.springcasemanagement.service.TeamService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("teams")
public class TeamResource {

    @Autowired
    TeamService teamService;

    @POST
    public Response getTeam(){
        teamService.getAll();
        return Response.ok().build();
    }
}
