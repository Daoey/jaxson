package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.UserService;

public class UserResourceImpl implements UserResource{

    @Autowired
    UserService userService;

    @Override
    public UserModel createUser(UserModel user) {
        User createdUser = userService.create(user.getUserNumber(), user.getUsername(),
                user.getFirstName(), user.getLastName());

        return new UserModel(createdUser.getId(), createdUser.getUserNumber(), createdUser.getUsername(),
                createdUser.getFirstName(), createdUser.getLastName());
    }

    @Override
    public UserModel updateUser(Long userNumber, UserModel user) {
        return null;
    }
}
