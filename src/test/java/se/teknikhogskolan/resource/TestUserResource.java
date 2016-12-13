package se.teknikhogskolan.resource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.springcasemanagement.model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class TestUserResource {

    private static Client client;
    private final String baseUrl = "http://localhost:8080/jaxson/";

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void canGetAllUsers() {
        Response response = client.target(baseUrl + "users").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetUserByUserNumber() {
        Response response = client.target(baseUrl + "users/1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canUpdateUser() {
        //Response response = client.target(baseUrl + "users/1").request().put();
    }

    @Test
    public void canCreateUser() throws Exception {
        //Response response = client.target(baseUrl + "users/1").request().post();
    }

    @Test
    public void canGetAllWorkItemsFromUser() {
        Response response = client.target(baseUrl + "users/1/workitems").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canAssignWorkItemToUser() {
        //Response response = client.target(baseUrl + "users/1/workitems").request().put();
    }
}