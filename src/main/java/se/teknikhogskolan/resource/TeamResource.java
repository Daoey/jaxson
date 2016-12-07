package se.teknikhogskolan.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("teams")
public class TeamResource {

    @POST
    public Response getTeam(){
        return Response.ok().build();
    }
}
