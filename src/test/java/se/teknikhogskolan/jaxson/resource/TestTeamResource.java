package se.teknikhogskolan.jaxson.resource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import se.teknikhogskolan.jaxson.JaxsonApplication;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.TeamViewBean;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.springcasemanagement.config.h2.H2InfrastructureConfig;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HsqlInfrastructureConfig.class })
public class TestTeamResource {

    private static Client client;
    @LocalServerPort private int randomPort;
    private WebTarget teamWebTarget;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setup() {
        String targetUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        String resource = "teams";
        teamWebTarget = client.target(targetUrl).path(resource);
    }

    @Test
    public void createTeam() {
        Response postResponse = client.target("http://localhost:" + randomPort + "/jaxson/").path("teams").request()
                .post(Entity.entity(new TeamViewBean("some description"), MediaType.APPLICATION_JSON));
        System.out.println(postResponse.getStatusInfo());
        System.out.println("read entity from response: " + postResponse.readEntity(String.class));

//        Response response = client.target("http://localhost:" + randomPort + "/jaxson/").path("teams").request().get();
//        System.out.println(response.hasEntity());
//
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetTeamById() {
//        TeamDto teamDto = new TeamDto("Testing", new ArrayList<>(), true);
//        Response response = client.target("http://localhost:" + randomPort + "/jaxson/").path("teams").request()
//                .post(Entity.entity(teamDto, MediaType.APPLICATION_JSON));
//        System.out.println(String.format("%s, %s, %s", response.getStatusInfo(), response.getLocation(), response.getMediaType()));
//        System.out.println(response.getHeaders().keySet());
//
//        Response response1 = teamWebTarget.request().get();
//        System.out.println(response1.readEntity(String.class));
//        List<TeamDto> all = teamWebTarget.request(MediaType.APPLICATION_XML).get(new GenericType<List<TeamDto>>() {});
//        System.out.println(all);
    }

    private static void assertNotEmpty(String string) {
        if (null == string || string.isEmpty()) fail();
    }
}
