package se.teknikhogskolan.jaxson.resource;

import javax.ws.rs.BeanParam;
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

import se.teknikhogskolan.jaxson.model.PageRequestBean;
import se.teknikhogskolan.jaxson.model.UserModel;

import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserResource {

    @POST
    Response createUser(UserModel user);

    @GET
    @Path("{userNumber}")
    UserModel getUserByUserNumber(@PathParam("userNumber") Long userNumber);

    @PUT
    @Path("{userNumber}")
    UserModel updateUser(@PathParam("userNumber") Long userNumber, UserModel user);

    @GET
    @Path("search")
    List<UserModel> getUserByParameter(@QueryParam("username") @DefaultValue("") String username, @QueryParam("firstname") @DefaultValue("") String firstname,
                                       @QueryParam("lastname") @DefaultValue("") String lastname);

    @DELETE
    @Path("{userNumber}")
    UserModel deleteUser(@PathParam("userNumber") Long userNumber);

    @GET
    List<UserModel> getAllByPage(@BeanParam PageRequestBean pageRequestBean);
    /* TODO
    *  Activate and inactive user
    *  Get all by creationDate
    * */
}
