package se.teknikhogskolan.jaxson.resource.implementation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.springcasemanagement.service.SecurityUserService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SecurityResourceImpl {

    private SecurityUserService userService;

    @Autowired
    public SecurityResourceImpl(SecurityUserService userService) {
        this.userService = userService;
    }

    @POST
    public void createUser(){
    }
    
}
