package se.teknikhogskolan.jaxson.resource;

import java.util.List;

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

import se.teknikhogskolan.jaxson.model.*;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserResource {

    @POST
    Response createUser(UserDto user);

    @GET
    @Path("{userNumber}")
    UserDto getUserByUserNumber(@PathParam("userNumber") Long userNumber);

    @PUT
    @Path("{userNumber}")
    UserDto updateUser(@PathParam("userNumber") Long userNumber,
                       @QueryParam("active") boolean active, UserDto user);

    @GET
    @Path("{userId}/workitems")
    List<WorkItemDto> getAllWorkItemsFromUser(@PathParam("userId") Long id);

    @GET
    List<UserDto> getAllByPage(@BeanParam PageRequestBean pageRequestBean,
                               @BeanParam UserRequestBean userRequestBean);

    @GET
    @Path("creation")
    List<UserDto> getByCreationDate(@BeanParam DateRequestBean dateRequestBean);
}