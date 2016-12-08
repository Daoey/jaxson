package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.UserService;
import se.teknikhogskolan.springcasemanagement.service.exception.DatabaseException;
import se.teknikhogskolan.springcasemanagement.service.exception.ForbiddenOperationException;
import se.teknikhogskolan.springcasemanagement.service.exception.InvalidInputException;
import se.teknikhogskolan.springcasemanagement.service.exception.NoSearchResultException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
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
        try{
            UserModel userModel = new UserModel(userService.create(user.getUserNumber(), user.getUsername(), user.getFirstName(), user.getLastName()));
            return Response.created(uriInfo.getAbsolutePathBuilder().path(userModel.getUserNumber().toString()).build()).build();
        } catch ( InvalidInputException e){
            throw new BadRequestException();
        }
    }

    @Override
    public UserModel getUserByUserNumber(Long userNumber) {
        try {
            return new UserModel(userService.getByUserNumber(userNumber));
        } catch (NoSearchResultException e){
            throw new NotFoundException();
        } catch (DatabaseException e){
            throw new ServerErrorException(e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public UserModel updateUser(Long userNumber, UserModel userModel) {
        User createdUser = null;
        try {
            if (userModel.getUsername() != null) {
                createdUser = userService.updateUsername(userNumber, userModel.getUsername());
            }
            if (userModel.getFirstName() != null) {
                createdUser = userService.updateFirstName(userNumber, userModel.getFirstName());
            }
            if (userModel.getLastName() != null) {
                createdUser = userService.updateLastName(userNumber, userModel.getLastName());
            }
        } catch (InvalidInputException e) {
            throw new BadRequestException();
        } catch (ForbiddenOperationException e) {
            throw new NotAllowedException("User inactive and can not be updated.");
        }


        if (createdUser != null) {
            return new UserModel(createdUser);
        } else {
            throw new BadRequestException();
        }
    }

    @Override
    public List<UserModel> getUserByParameter(String username, String firstname, String lastname) {
        List<UserModel> userModels = new ArrayList<>();
        try {
            userService.search(firstname, lastname, username).forEach(user -> userModels.add(new UserModel(user)));
        } catch (NoSearchResultException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        }
        return userModels;
    }
}