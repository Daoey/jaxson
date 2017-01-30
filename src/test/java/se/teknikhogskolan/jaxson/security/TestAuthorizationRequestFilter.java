package se.teknikhogskolan.jaxson.security;

import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import se.teknikhogskolan.jaxson.JaxsonApplication;
import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {HsqlInfrastructureConfig.class})
@SqlGroup({
        @Sql(scripts = "../resource/insert_team.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "../resource/hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
public class TestAuthorizationRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static Client client;
    @LocalServerPort
    private int randomPort;
    private WebTarget teamWebTarget;

    // Data from insert_team.sql
    private final Long turtlesTeamId = 2001L;
    private final Long clanTeamId = 2002L;
    private String token;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setup() {
        String targetUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        String resource = "teams";
        teamWebTarget = client.target(targetUrl).path(resource);
        
        Response result = client.target(targetUrl).path("register").request()
                .post(Entity.entity(new Credentials("username", "password"), MediaType.APPLICATION_JSON));

        String responseAsString = result.readEntity(String.class);

        JSONObject jsonObj = new JSONObject(responseAsString);
        System.out.println(jsonObj);
//        token = "Bearer " + jsonObj.get
    }

    @Test
    public void getTeamWithoutAuthorizationShouldReturn403() {
        Response response = teamWebTarget.request().get();
        assertEquals(UNAUTHORIZED, response.getStatusInfo());
    }

    @Test
    public void canGetTeamsWhenAuthorized() {
        Response response = teamWebTarget.request().header(AUTHORIZATION, token).get();
        assertEquals(OK, response.getStatusInfo());
        List<TeamDto> result = response.readEntity(new GenericType<List<TeamDto>>(){});
        List<Long> teamIdsInDatabase = result.stream().map(TeamDto::getId).collect(Collectors.toList());
        assertTrue(teamIdsInDatabase.contains(turtlesTeamId));
        assertTrue(teamIdsInDatabase.contains(clanTeamId));
    }
}
