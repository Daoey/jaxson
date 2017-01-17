package se.teknikhogskolan.jaxson.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.Credentials;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SecurityResource {

    @POST
    @Path("register")
    Response createUser(Credentials credentials);

    @POST
    @Path("login")
    Response authenticateUser(Credentials credentials);

    @POST
    @Path("Tokens/{token}")
    Response refreshToken(@PathParam("token") String token);
}
