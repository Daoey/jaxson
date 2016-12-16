package se.teknikhogskolan.jaxson.resource;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.teknikhogskolan.jaxson.model.DateRequestBean;
import se.teknikhogskolan.jaxson.model.PageRequestBean;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.model.UserRequestBean;
import se.teknikhogskolan.jaxson.model.WorkItemDto;


@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserResource {

    @POST
    Response createUser(UserDto user);

    @GET
    @Path("{userNumber}")
    Response getUserByUserNumber(@PathParam("userNumber") Long userNumber);

    @PUT
    @Path("{userNumber}")
    Response updateUser(@PathParam("userNumber") Long userNumber, UserDto user);

    @GET
    Response getAll(@BeanParam PageRequestBean pageRequestBean,
                         @BeanParam UserRequestBean userRequestBean);

    @GET
    @Path("{userNumber}/workitems")
    Response getAllWorkItemsFromUser(@PathParam("userNumber") Long userNumber);

    @PUT
    @Path("{userNumber}/workItems")
    Response assignWorkItemToUser(@PathParam("userNumber") Long userNumber, WorkItemDto workItemDto);
}