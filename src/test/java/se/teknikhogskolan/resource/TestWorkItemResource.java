package se.teknikhogskolan.resource;

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
        
        System.out.println("Port: " + randomPort);

        client.target("http://localhost:" + randomPort + "/jaxson/").path("workitems").request()
                .post(Entity.entity(new WorkItemDto("some description"), MediaType.APPLICATION_JSON));

        Response response = client.target("http://localhost:" + randomPort + "/jaxson/").path("workitems/1").request()
                .get();
                
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}