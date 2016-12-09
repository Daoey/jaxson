package se.teknikhogskolan.jaxson.resource.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.*;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.UserService;
import se.teknikhogskolan.springcasemanagement.service.exception.DatabaseException;
import se.teknikhogskolan.springcasemanagement.service.exception.InvalidInputException;
import se.teknikhogskolan.springcasemanagement.service.exception.NoSearchResultException;

public class UserResourceImpl implements UserResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    UserService userService;

    @Override
    public Response createUser(UserDto user) {
        try {
            UserDto userDto = new UserDto(userService.create(user.getUserNumber(),
                    user.getUsername(), user.getFirstName(), user.getLastName()));
            return Response.created(uriInfo.getAbsolutePathBuilder()
                    .path(userDto.getUserNumber().toString()).build()).build();
        } catch (InvalidInputException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public UserDto getUserByUserNumber(Long userNumber) {
        return execute(userService1 -> userService1.getByUserNumber(userNumber));
    }

    @Override
    public UserDto updateUser(Long userNumber, boolean active, UserDto userDto) {
        UserDto createdUser = null;
        if (userDto.getUsername() != null) {
            createdUser = execute(userService1 -> userService1.updateUsername(userNumber, userDto.getUsername()));
        }
        if (userDto.getFirstName() != null) {
            createdUser = execute(userService1 -> userService1.updateFirstName(userNumber, userDto.getFirstName()));
        }
        if (userDto.getLastName() != null) {
            createdUser = execute(userService1 -> userService1.updateLastName(userNumber, userDto.getLastName()));
        }

        if (createdUser != null) {
            return createdUser;
        } else {
            throw new BadRequestException();
        }
    }

    @Override
    public List<UserDto> getAllByPage(@BeanParam PageRequestBean pageRequestBean, @BeanParam UserRequestBean userRequestBean) {
        return null;
    }

    @Override
    public List<WorkItemDto> getAllWorkItemsFromUser(Long id) {
        return null;
    }

    @Override
    public List<UserDto> getByCreationDate(@BeanParam DateRequestBean dateRequestBean) {
        return executeMany(userService1 -> userService1.getByCreationDate(dateRequestBean.getStartDate(),
                dateRequestBean.getEndDate()));
    }

    private List<UserDto> executeMany(Function<UserService, List<User>> operation) {
        List<UserDto> userDtos = new ArrayList<>();
        try {
            operation.apply(userService).forEach(user -> userDtos.add(new UserDto(user)));
            return userDtos;
        } catch (NoSearchResultException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        }
    }

    private UserDto execute(Function<UserService, User> operation) {
        try {
            return new UserDto(operation.apply(userService));
        } catch (NoSearchResultException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        }
    }
}