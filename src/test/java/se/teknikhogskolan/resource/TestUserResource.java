package se.teknikhogskolan.resource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class TestUserResource {

    private static Client client;
    private final String baseUrl = "http://localhost:8080/jaxson/";

    @BeforeClass
    public static void setUp(){
        client = ClientBuilder.newClient();
    }

    @Test
    public void canGetAllUsers(){
        Response response = client.target(baseUrl + "users").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
