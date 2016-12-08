package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.PageRequestBean;
import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.UserService;
import se.teknikhogskolan.springcasemanagement.service.exception.DatabaseException;
import se.teknikhogskolan.springcasemanagement.service.exception.ForbiddenOperationException;
import se.teknikhogskolan.springcasemanagement.service.exception.InvalidInputException;
import se.teknikhogskolan.springcasemanagement.service.exception.NoSearchResultException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
            return execute(userService1 -> userService1.getByUserNumber(userNumber));
    }

    @Override
    public UserModel updateUser(Long userNumber, UserModel userModel) {
        UserModel createdUser = null;
        if (userModel.getUsername() != null) {
            createdUser = execute(userService1 -> userService1.updateUsername(userNumber, userModel.getUsername()));
        }
        if (userModel.getFirstName() != null) {
            createdUser = execute(userService1 -> userService1.updateFirstName(userNumber, userModel.getFirstName()));
        }
        if (userModel.getLastName() != null) {
            createdUser = execute(userService1 -> userService1.updateLastName(userNumber, userModel.getLastName()));
        }

        if (createdUser != null) {
            return createdUser;
        } else {
            throw new BadRequestException();
        }
    }

    @Override
    public List<UserModel> getUserByParameter(String username, String firstname, String lastname) {
            return executeMany(userService1 -> userService1.search(firstname, lastname, username));
    }

    @Override
    public UserModel deleteUser(Long userNumber) {
        return null;
    }

    @Override
    public List<UserModel> getAllByPage(@BeanParam PageRequestBean pageRequestBean) {
        return executeMany(userService1 -> userService1.getAllByPage(pageRequestBean.getPage(), pageRequestBean.getSize()).getContent());
    }

    private List<UserModel> executeMany(Function<UserService, List<User>> operation){
        List<UserModel> userModels = new ArrayList<>();
        try {
            operation.apply(userService).forEach(user -> userModels.add(new UserModel(user)));
            return userModels;
        } catch (NoSearchResultException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        }
    }

    private UserModel execute(Function<UserService, User> operation){
        try {
            return new UserModel(operation.apply(userService));
        } catch (NoSearchResultException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        }
    }
}