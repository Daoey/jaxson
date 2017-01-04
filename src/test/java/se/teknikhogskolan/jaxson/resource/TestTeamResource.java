package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import se.teknikhogskolan.jaxson.exception.ErrorMessage;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {HsqlInfrastructureConfig.class})
@SqlGroup({
        @Sql(scripts = "insert_team.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
public class TestTeamResource {

    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_CODE = "Basic cm9vdDpzZWNyZXQ=";
    private static Client client;
    @LocalServerPort private int randomPort;
    private WebTarget teamWebTarget;

    // Data from insert_team.sql
    private final Long turtlesTeamId = 2001L;
    private final Long clanTeamId = 2002L;
    private TeamDto turtlesTeam;
    private TeamDto clanTeam;
    private TeamDto inactiveTeam;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setup() {
        String targetUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        String resource = "teams";
        teamWebTarget = client.target(targetUrl).path(resource);
        getTeamsFromDatabase();
    }

    private void getTeamsFromDatabase() {
        try { // TODO remove try-catch when possible
            turtlesTeam = teamWebTarget.path(turtlesTeamId.toString()).request()
                    .header(AUTHORIZATION, AUTHORIZATION_CODE).get(TeamDto.class);
            clanTeam = teamWebTarget.path(clanTeamId.toString()).request().header(AUTHORIZATION, AUTHORIZATION_CODE)
                    .get(TeamDto.class);
            String inactiveTeamId = "2003";
            inactiveTeam = teamWebTarget.path(inactiveTeamId).request().header(AUTHORIZATION, AUTHORIZATION_CODE)
                    .get(TeamDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUsersFromTeamWithoutUsersShouldReturnEmptyList() {
        Response response = teamWebTarget.path(clanTeam.getId().toString() + "/users").request()
                .header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        assertEquals(OK, response.getStatusInfo());
        List<UserDto> result = response.readEntity(new GenericType<List<UserDto>>(){});
        assertTrue(result.isEmpty());
    }

    @Test
    public void canGetUsersFromInactiveTeam() {
        assertFalse(inactiveTeam.isActive());
        Response response = teamWebTarget.path(inactiveTeam.getId().toString() + "/users").request()
                .header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        assertEquals(OK, response.getStatusInfo());
        List<UserDto> result = response.readEntity(new GenericType<List<UserDto>>(){});
        assertTrue(result.isEmpty());
    }

    @Test
    public void canGetUsersFromTeam() {
        Response response = teamWebTarget.path(turtlesTeam.getId().toString() + "/users").request()
                .header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        assertEquals(OK, response.getStatusInfo());
        List<UserDto> result = response.readEntity(new GenericType<List<UserDto>>(){});
        List<String> firstNamesInResult = result.stream().map(UserDto::getFirstName).collect(Collectors.toList());
        assertTrue(firstNamesInResult.contains("Leonardo"));
        assertTrue(firstNamesInResult.contains("Raffaello"));
        assertTrue(firstNamesInResult.contains("Michelangelo"));
        assertTrue(firstNamesInResult.contains("Donatello"));
        assertTrue(firstNamesInResult.contains("Splinter"));
    }

    @Test
    public void canGetAllTeams() {
        Response response = teamWebTarget.request().header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        assertEquals(OK, response.getStatusInfo());
        List<TeamDto> result = response.readEntity(new GenericType<List<TeamDto>>(){});
        assertTrue(result.contains(turtlesTeam));
        assertTrue(result.contains(clanTeam));
    }

    @Test
    @Sql("hsql_clean_tables.sql") // TODO move to mocking test
    public void getAllTeamsShouldReturnEmptyListIfNoTeamExist() {
        Response response = teamWebTarget.request().header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        assertEquals(OK, response.getStatusInfo());
        List<TeamDto> result = response.readEntity(new GenericType<List<TeamDto>>(){});
        assertTrue(result.isEmpty());
    }

    @Test
    public void createTeamReturnsLocation() {
        TeamDto teamDto = new TeamDto("Testing Team");
        URI teamInDbLocation = teamWebTarget.request().header(AUTHORIZATION, AUTHORIZATION_CODE)
                .post(Entity.entity(teamDto, MediaType.APPLICATION_JSON)).getLocation();
        assertNotNull(teamInDbLocation);

        Response response = client.target(teamInDbLocation).request().header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        TeamDto result = response.readEntity(TeamDto.class);
        assertEquals(teamDto, result);
    }

    @Test
    public void createAlreadyPersistedTeamShouldReturnForbidden() {
        Response response = teamWebTarget.request().header(AUTHORIZATION, AUTHORIZATION_CODE)
                .post(Entity.entity(turtlesTeam, MediaType.APPLICATION_JSON));
        assertEquals(FORBIDDEN, response.getStatusInfo());
    }

    @Test
    public void creatingTwoTeamsWithSameNameShouldReturnErrorMessage() {
        String turtlesTeamName = turtlesTeam.getName();
        Response response = teamWebTarget.request().header(AUTHORIZATION, AUTHORIZATION_CODE)
                .post(Entity.entity(new TeamDto(turtlesTeamName), MediaType.APPLICATION_JSON));
        ErrorMessage errorMessage = response.readEntity(ErrorMessage.class);

        assertEquals(PRECONDITION_FAILED, response.getStatusInfo());
        assertEquals(PRECONDITION_FAILED.getStatusCode(), errorMessage.getCode());
        assertEquals(PRECONDITION_FAILED.toString(), errorMessage.getStatus());
        String errorText = "Team with name '" + turtlesTeamName + "' already exist.";
        assertEquals(errorText, errorMessage.getMessage());
    }

    @Test
    public void getNotExistingTeamShouldReturn404() {
        final String nonExistingTeam = "8489584656";
        Response response = teamWebTarget.path(nonExistingTeam).request().header(AUTHORIZATION, AUTHORIZATION_CODE)
                .get();
        assertEquals(NOT_FOUND, response.getStatusInfo());
    }

    @Test
    public void canGetTeamById() {
        Response response = teamWebTarget.path(turtlesTeamId.toString()).request()
                .header(AUTHORIZATION, AUTHORIZATION_CODE).get();
        assertEquals(OK, response.getStatusInfo());
        TeamDto result = response.readEntity(TeamDto.class);
        assertEquals(turtlesTeam.getName(), result.getName());
    }
}