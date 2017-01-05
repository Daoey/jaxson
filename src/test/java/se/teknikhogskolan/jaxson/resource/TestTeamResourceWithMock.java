package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.teknikhogskolan.jaxson.exception.ErrorMessage;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTeamResourceWithMock {

    private static String baseUrl;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private Team mockedTeam;

    @Mock
    private User mockedUser;

    @MockBean
    private TeamService teamService;

    @MockBean
    private UserService userService;

    @MockBean
    private WorkItemService workItemService;

    @LocalServerPort
    private int randomPort;

    private RestTemplate restTemplate;
    private final String auth = "Authorization";
    private final String authCode = "Basic cm9vdDpzZWNyZXQ=";
    private final String teamResource = "/teams/";
    private final String teamName = "The mockers";
    private final Team team = new Team(teamName);
    private final Long teamId = 1001L;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + randomPort + "/jaxson";
        restTemplate = restTemplateBuilder.build();
    }

    @Test
    public void canGetWorkItemsInTeam() {
        Collection<WorkItem> workItems = new ArrayList<>();
        String workItemDescription = "Test get all WorkItems in a Team";
        WorkItem workItem = new WorkItem(workItemDescription);
        workItems.add(workItem);
        given(workItemService.getByTeamId(teamId)).willReturn(workItems);
        ResponseEntity<WorkItemDto[]> response = restTemplate
                .exchange(createUri(teamResource + teamId + "/workitems"), GET, createHttpEntity(null, null),
                        WorkItemDto[].class);
        WorkItemDto workItemInResponse = Arrays.asList(response.getBody()).get(0);
        assertEquals(workItemDescription, workItemInResponse.getDescription());
    }

    @Test
    public void canGetUsersInTeam() {
        List<User> users = new ArrayList<>();
        users.add(mockedUser);
        given(userService.getAllByTeamId(teamId)).willReturn(users);
        ResponseEntity<UserDto[]> response = restTemplate
                .exchange(createUri(teamResource + teamId + "/users"), GET,
                        createHttpEntity(null, null), UserDto[].class);
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void canGetAllTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(mockedTeam);
        given(teamService.getAll()).willReturn(teams);
        ResponseEntity<TeamDto[]> response = restTemplate
                .exchange(createUri(teamResource), GET, createHttpEntity(null, null), TeamDto[].class);
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void getAllTeamsShouldReturnEmptyListIfNoTeamExist() {
        given(teamService.getAll()).willReturn(new ArrayList<>());
        ResponseEntity<TeamDto[]> response = restTemplate
                .exchange(createUri(teamResource), GET, createHttpEntity(null, null), TeamDto[].class);
        assertEquals(0, response.getBody().length);
    }

    @Test
    public void getAllTeamsShouldReturnEmptyArrayIfNoTeamsExist() {
        given(teamService.getAll()).willReturn(new ArrayList<>());
        ResponseEntity<Collection> response = restTemplate
                .exchange(createUri(teamResource), GET, createHttpEntity(null, null), Collection.class);
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void canUpdateTeam() {
        String oldName = "Old team name";
        given(teamService.getById(teamId)).willReturn(mockedTeam);
        when(mockedTeam.isActive()).thenReturn(true);
        when(mockedTeam.getName()).thenReturn(oldName);

        JSONObject newTeamValues = new JSONObject();
        newTeamValues.put("name", "New much cooler name");
        newTeamValues.put("active", false);

        ResponseEntity<Void> response = restTemplate
                .exchange(createUri(teamResource + teamId), PUT, createHttpEntity(newTeamValues, null), Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void notAllowedToChangeTeamId() {
        given(teamService.getById(teamId)).willReturn(mockedTeam);
        when(mockedTeam.isActive()).thenReturn(false);
        Long oldId = 16546312L;
        when(mockedTeam.getId()).thenReturn(oldId);

        JSONObject newTeamValues = new JSONObject();
        Long newId = 2365L;
        newTeamValues.put("id", newId);
        newTeamValues.put("active", true);

        restTemplate.setErrorHandler(new ResponseErrorIgnorer());

        ResponseEntity<ErrorMessage> response = restTemplate
                    .exchange(createUri(teamResource + teamId), PUT, createHttpEntity(newTeamValues, null),
                            ErrorMessage.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        String expectedMessage = "Not allowed to update id on Team. Team id was '1001', id in request body was '2365'";
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    public void canAddUserToTeamUsingUsernumber() {
        Long usernumber = 1001L;
        Long userId = 64648949L;
        given(userService.getByUserNumber(usernumber)).willReturn(mockedUser);
        when(mockedUser.getId()).thenReturn(userId);
        given(teamService.addUserToTeam(teamId, userId)).willReturn(mockedTeam);

        JSONObject userToAdd = new JSONObject();
        userToAdd.put("userNumber", usernumber);

        ResponseEntity<Void> response = restTemplate.exchange(createUri(
                teamResource + teamId + "/users"), PUT, createHttpEntity(userToAdd, null), Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void canAddUserToTeamUsingUserId() {
        Long userId = 1001L;
        given(teamService.addUserToTeam(teamId, userId)).willReturn(mockedTeam);

        JSONObject userToAdd = new JSONObject();
        userToAdd.put("id", "1001");

        ResponseEntity<Void> response = restTemplate
                .exchange(createUri(teamResource + teamId + "/users"), PUT,
                        createHttpEntity(userToAdd, null), Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void createTeamWithUsersShouldReturnForbidden() throws URISyntaxException {
        exception.expect(HttpClientErrorException.class);
        exception.expectMessage("403 null");

        JSONObject teamToCreate = new JSONObject();
        teamToCreate.put("name", mockedTeam.getName());
        Collection<Long> usersId = new ArrayList<>();
        usersId.add(24564L);
        teamToCreate.put("usersId", usersId);

        restTemplate.exchange(createUri(teamResource), POST, createHttpEntity(teamToCreate, null), String.class);
    }

    @Test
    public void createTeamPassingTeamIdShouldReturnForbidden() throws URISyntaxException {
        exception.expect(HttpClientErrorException.class);
        exception.expectMessage("403 null");

        JSONObject teamToCreate = new JSONObject();
        teamToCreate.put("name", mockedTeam.getName());
        teamToCreate.put("id", 216516L);

        restTemplate.exchange(createUri(teamResource), POST, createHttpEntity(teamToCreate, null), String.class);
    }

    @Test
    public void createTeam() throws URISyntaxException {
        given(teamService.create(mockedTeam.getName())).willReturn(mockedTeam);
        when(mockedTeam.getId()).thenReturn(teamId);

        JSONObject teamToCreate = new JSONObject();
        teamToCreate.put("name", mockedTeam.getName());
        teamToCreate.put("active", true);

        ResponseEntity<String> response = restTemplate
                .exchange(createUri(teamResource), POST, createHttpEntity(teamToCreate, null), String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String expectedLocation = "/jaxson" + teamResource + teamId;
        assertTrue(response.getHeaders().getLocation().toString().endsWith(expectedLocation));
    }

    @Test
    public void getTeamById() throws URISyntaxException {
        given(teamService.getById(teamId)).willReturn(team);
        ResponseEntity<TeamDto> response = restTemplate.exchange(
                createUri(teamResource + teamId), GET, createHttpEntity(null, null), TeamDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new TeamDto(team), response.getBody());
    }

    private URI createUri(String resource) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + resource);
        return builder.build().encode().toUri();
    }

    private HttpEntity<?> createHttpEntity(JSONObject body, Map<String, Object> headParameters) {
        HttpHeaders headers = new HttpHeaders();
        if (null != headParameters) headParameters.forEach((key, value) -> headers.set(key, value.toString()));
        headers.set(auth, authCode);
        headers.set("Accept", MediaType.APPLICATION_JSON);
        headers.set("Content-Type", MediaType.APPLICATION_JSON);
        return (null == body) ? new HttpEntity<>(headers) : new HttpEntity<>(body.toString(), headers);
    }

    private static class ResponseErrorIgnorer implements ResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse clienthttpresponse) throws IOException {
            // Do nothing
        }

        @Override
        public boolean hasError(ClientHttpResponse clienthttpresponse) throws IOException {

            if (clienthttpresponse.getStatusCode() != HttpStatus.OK) {
                // has error
                // filter response codes
                if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
                    // let 403 slip through
                    return false;
                }
            }
            return true;
        }
    }
}