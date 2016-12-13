package se.teknikhogskolan.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.repository.UserRepository;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class TestUserResource {

    private static Client client;
    private final String baseUrl = "http://localhost:8080/jaxson/";
    private UserDto userInDb;
    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void canGetAllUsers() {
        Response response = client.target(baseUrl).path("users").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetUserByUserNumber() {
        Response response = client.target(baseUrl).path("users/1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canUpdateUser() {
        //Response response = client.target(baseUrl).path("users/1").request().put();
    }

    @Test
    public void canCreateUser() throws Exception {
        Response response = client.target(baseUrl).path("users").request()
                .post(Entity.entity(new User(457L, "canCreateUser", "temp", "temp")
                        , MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetAllWorkItemsFromUser() {
        Response response = client.target(baseUrl).path("users/1/workitems").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canAssignWorkItemToUser() {
        //Response response = client.target(baseUrl).path("users/1/workitems").request().put();
    }
}