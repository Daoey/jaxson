package se.teknikhogskolan.jaxson.resource.implementation;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.exception.ForbiddenOperationException;
import se.teknikhogskolan.jaxson.exception.IncompleteException;
import se.teknikhogskolan.jaxson.model.PageRequestBean;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.model.UserRequestBean;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.UserService;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

public final class UserResourceImpl implements UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkItemService workItemService;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response createUser(UserDto userDto) {
        if (noNullParameters(userDto)) {
            User userDao = userService.create(userDto.getUserNumber(),
                    userDto.getUsername(), userDto.getFirstName(), userDto.getLastName());
            return Response.created(uriInfo.getAbsolutePathBuilder()
                    .path(userDao.getUserNumber().toString()).build()).build();
        }
        throw new IllegalArgumentException("Can not create User item without JSON body containing"
                + " UserNumber, username, firstname and lastname");
    }

    private boolean noNullParameters(UserDto user) {
        return user.getUserNumber() != null && user.getUsername() != null
                && user.getFirstName() != null && user.getLastName() != null;
    }

    @Override
    public Response getUserByUserNumber(Long userNumber) {
        UserDto userDto = new UserDto(userService.getByUserNumber(userNumber));
        return Response.ok(userDto).build();
    }

    @Override
    public Response updateUser(Long userNumber, UserDto userDto) {
        User userDao = userService.getByUserNumber(userNumber);
        if (updatable(userDao, userDto)) {
            if (getUpdatedUser(userDao, userDto, userNumber) != null) {
                return Response.noContent().location(uriInfo.getAbsolutePathBuilder()
                        .path(userDto.getUserNumber().toString()).build()).build();
            } else {
                throw new IncompleteException("Could not find any username,"
                        + " firstname or lastname in the JSON body of the request.");
            }
        } else {
            throw new ForbiddenOperationException(String.format(
                    "Cannot update User with id '%d'. Team is inactive.", userDto.getId()));
        }
    }

    private boolean updatable(User userDao, UserDto userDto) {
        return userDao.isActive() | userDto.isActive();
    }


    private User getUpdatedUser(User userDao, UserDto userDto, Long userNumber) {
        if (!userDao.isActive()) {
            userService.activate(userNumber);
        }
        User createdUser = updateUserInformation(userDto, userNumber);
        if (!userDto.isActive()) {
            userService.inactivate(userNumber);
        }
        return createdUser;
    }


    private User updateUserInformation(UserDto userDto, Long userNumber) {
        User createdUser = null;
        if (userDto.getUsername() != null) {
            createdUser = userService.updateUsername(userNumber, userDto.getUsername());
        }
        if (userDto.getFirstName() != null) {
            createdUser = userService.updateFirstName(userNumber, userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            createdUser = userService.updateLastName(userNumber, userDto.getLastName());
        }
        return createdUser;
    }

    @Override
    public Response getAll(@BeanParam PageRequestBean pageRequestBean,
                           @BeanParam UserRequestBean userRequestBean) {
        List<UserDto> userDtos = new ArrayList<>();
        if (onlyDefaultValues(userRequestBean)) {
            userService.getAllByPage(pageRequestBean.getPage(),
                    pageRequestBean.getSize()).forEach(user -> userDtos.add(new UserDto(user)));
        } else {
            userService.search(userRequestBean.getFirtname(), userRequestBean.getLastname(),
                    userRequestBean.getUsername()).forEach(user -> userDtos.add(new UserDto(user)));
        }
        return Response.ok(userDtos).build();
    }

    private boolean onlyDefaultValues(UserRequestBean userRequestBean) {
        String defaultValue = "";
        return defaultValue.equals(userRequestBean.getUsername())
                && defaultValue.equals(userRequestBean.getFirtname())
                && defaultValue.equals(userRequestBean.getLastname());
    }

    @Override
    public Response getAllWorkItemsFromUser(Long userNumber) {
        List<WorkItemDto> workItemDtos = new ArrayList<>();
        workItemService.getByUsernumber(userNumber).forEach(workItem -> workItemDtos.add(new WorkItemDto(workItem)));
        return Response.ok(workItemDtos).build();
    }

    @Override
    public Response assignWorkItemToUser(Long userNumber, WorkItemDto workItemDto) {
        if (workItemDto.getId() != null) {
            workItemService.setUser(userNumber, workItemDto.getId());
            return Response.noContent().location(uriInfo.getAbsolutePathBuilder()
                    .path(userNumber.toString()).build()).build();
        }
        throw new IllegalArgumentException("Could not find any WorkItem id in Json Body of the request.");
    }
}