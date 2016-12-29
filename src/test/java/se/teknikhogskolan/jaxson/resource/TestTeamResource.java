package se.teknikhogskolan.jaxson.resource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import se.teknikhogskolan.jaxson.JaxsonApplication;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.springcasemanagement.config.h2.H2InfrastructureConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {H2InfrastructureConfig.class})
public class TestTeamResource {

    @LocalServerPort private int randomPort;
    private static final String auth = "Authorization";
    private static final String authCode = "Basic cm9vdDpzZWNyZXQ=";
    private static Client client;
    private WebTarget teamWebTarget;
    private URI teamInDbLocation;
    private TeamDto teamDto;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setup() {
        String targetUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        String resource = "teams";
        teamWebTarget = client.target(targetUrl).path(resource);
        teamDto = new TeamDto("Testing Team");
        teamInDbLocation = teamWebTarget.request().header(auth, authCode)
                .post(Entity.entity(teamDto, MediaType.APPLICATION_JSON)).getLocation();
    }

    @Test
    public void createTeam() {
        Response response = teamWebTarget.request().header(auth, authCode)
                .post(Entity.entity(new TeamDto("Created Team"), MediaType.APPLICATION_JSON));
        assertEquals(CREATED, response.getStatusInfo());
        assertNotNull(response.getLocation());
    }

    @Test
    public void canGetTeamById() {
        Response response = client.target(teamInDbLocation).request().header(auth, authCode).get();
        assertEquals(OK, response.getStatusInfo());
        TeamDto result = response.readEntity(TeamDto.class);
        assertEquals(teamDto.getName(), result.getName());
    }
}