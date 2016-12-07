package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.service.TeamService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Autowired
    TeamService teamService;

    @GET
    @Path("{id}")
    public Response getTeam(@PathParam("id") Long id){
        Team team = teamService.getById(id);
        return Response.ok(team).build();
    }

    @POST
    public Response createTeam(String name){
        teamService.create(name);
        return Response.status(Response.Status.CREATED).build();
    }
}
