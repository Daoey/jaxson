package se.teknikhogskolan.jaxson.resource.implementation;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.exception.IncompleteException;
import se.teknikhogskolan.jaxson.model.DateRequestBean;
import se.teknikhogskolan.jaxson.model.PageRequestBean;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.model.UserRequestBean;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.jaxson.resource.UserResource;
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
    public Response createUser(UserDto user) {
        UserDto userDto = new UserDto(userService.create(user.getUserNumber(),
                user.getUsername(), user.getFirstName(), user.getLastName()));
        return Response.created(uriInfo.getAbsolutePathBuilder()
                .path(userDto.getUserNumber().toString()).build()).build();
    }

    @Override
    public Response getUserByUserNumber(Long userNumber) {
        UserDto userDto = new UserDto(userService.getByUserNumber(userNumber));
        return Response.ok(userDto).build();
    }

    @Override
    public Response updateUser(Long userNumber, boolean active, UserDto userDto) {
        UserDto updatedUser = updateUserInformation(userDto, userNumber);
        if (updatedUser != null) {
            return Response.noContent().location(uriInfo.getAbsolutePathBuilder()
                    .path(userDto.getUserNumber().toString()).build()).build();
        } else {
            //TODO update this to the most resent exception
            throw new IncompleteException("Could not find any username,"
                    + " firstname or lastname in the request.");
        }
    }

    @Override
    public Response getAll(@BeanParam PageRequestBean pageRequestBean,
                           @BeanParam UserRequestBean userRequestBean) {
        List<UserDto> userDtos = new ArrayList<>();
        if (!userRequestBean.getUsername().equals("") || !userRequestBean.getFirtname().equals("")
                || !userRequestBean.getLastname().equals("")) {
            userService.search(userRequestBean.getFirtname(), userRequestBean.getLastname(),
                    userRequestBean.getUsername()).forEach(user -> userDtos.add(new UserDto(user)));
        } else {
            userService.getAllByPage(pageRequestBean.getPage(),
                    pageRequestBean.getSize()).forEach(user -> userDtos.add(new UserDto(user)));
        }
        return Response.ok(userDtos).build();
    }

    @Override
    public List<UserDto> getByCreationDate(@BeanParam DateRequestBean dateRequestBean) {
        List<UserDto> userDtos = new ArrayList<>();
        userService.getByCreationDate(dateRequestBean.getStartDate(),
                dateRequestBean.getEndDate()).forEach(user -> userDtos.add(new UserDto(user)));
        return userDtos;
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
            return Response.noContent().location(uriInfo.getAbsolutePathBuilder()
                    .path(userNumber.toString()).build()).build();
        }
        //TODO change to the most resent exception
        throw new IncompleteException("Could not find any WorkItem id in the request.");
    }

    private UserDto updateUserInformation(UserDto userDto, Long userNumber) {
        UserDto createdUser = null;
        if (userDto.getUsername() != null) {
            createdUser = new UserDto(userService.updateUsername(userNumber, userDto.getUsername()));
        }
        if (userDto.getFirstName() != null) {
            createdUser = new UserDto(userService.updateFirstName(userNumber, userDto.getFirstName()));
        }
        if (userDto.getLastName() != null) {
            createdUser = new UserDto(userService.updateLastName(userNumber, userDto.getLastName()));
        }
        return createdUser;
    }
}