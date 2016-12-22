package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import se.teknikhogskolan.jaxson.JaxsonApplication;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HsqlInfrastructureConfig.class })
public class TestWorkItemResource {

    private static Client client;

    @LocalServerPort
    private int randomPort;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Test
    public void addAWorkItemAndGetItFromInMemoryDatabase() {
        String baseUrl = "http://localhost:" + randomPort + "/jaxson/";
        String resource = "workitems";
        URI workItemLocation = client.target(baseUrl).path(resource).request().header("Authorization", "Basic cm9vdDpzZWNyZXQ=").post(Entity.entity(
                new WorkItemDto("some description"), MediaType.APPLICATION_JSON)).getLocation();
        Response result = client.target(workItemLocation).request().header("Authorization", "Basic cm9vdDpzZWNyZXQ=").get();
        assertEquals(OK, result.getStatusInfo());
    }
}