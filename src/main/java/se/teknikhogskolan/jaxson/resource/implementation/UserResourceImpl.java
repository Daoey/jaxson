package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.UserModel;
import se.teknikhogskolan.jaxson.resource.UserResource;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

import javax.ws.rs.core.Response;
import java.util.List;

public class UserResourceImpl implements UserResource{

    @Autowired
    UserService userService;

    @Override
    public Response createUser(UserModel user) {
        return null;
    }

    @Override
    public UserModel getUserByUserNumber(Long userNumber) {
        return null;
    }

    @Override
    public UserModel updateUser(Long userNumber, UserModel user) {
        return null;
    }

    @Override
    public List<UserModel> getUserByParameter(String username, String firstname, String lastname) {
        return null;
    }
}
