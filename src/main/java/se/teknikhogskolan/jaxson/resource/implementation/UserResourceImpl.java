package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class UserResourceImpl implements UserResource {

    @Context
    UriInfo uriInfo;

    @Autowired
    UserService userService;

    @Override
    public Response createUser(UserModel user) {
        UserModel userModel = new UserModel(userService.create(user.getUserNumber(), user.getUsername(), user.getFirstName(), user.getLastName()));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(userModel.getUserNumber().toString()).build()).build();
    }

    @Override
    public UserModel getUserByUserNumber(Long userNumber) {
        return new UserModel(userService.getByUserNumber(userNumber));
    }

    @Override
    public UserModel updateUser(Long userNumber, UserModel userModel) {
        User createdUser = null;
        if (userModel.getUsername() != null) {
            createdUser = userService.updateUsername(userNumber, userModel.getUsername());
        }
        if (userModel.getFirstName() != null) {
            createdUser = userService.updateFirstName(userNumber, userModel.getFirstName());
        }
        if (userModel.getLastName() != null) {
            createdUser = userService.updateLastName(userNumber, userModel.getLastName());
        }

        if(createdUser != null){
            return new UserModel(createdUser);
        }else {
            throw new WebApplicationException();
        }
    }

    @Override
    public List<UserModel> getUserByParameter(String username, String firstname, String lastname) {
        List<UserModel> userModels = new ArrayList<>();
        userService.search(firstname, lastname, username).forEach(user -> userModels.add(new UserModel(user)));
        return userModels;
    }
}