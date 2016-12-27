package se.teknikhogskolan.jaxson.resource;

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
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTeamResourceWithMock {

    @MockBean
    private TeamService teamService;

    @MockBean
    private UserService userService;

    @Mock
    Team mockedTeam;

    @Mock
    User mockedUser;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int randomPort;

    private static String baseUrl;
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

        assertEquals(NO_CONTENT, response.getStatusCode());
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

        restTemplate.setErrorHandler(new responseErrorIgnorer());

        ResponseEntity<String> response = restTemplate
                    .exchange(createUri(teamResource + teamId), PUT, createHttpEntity(newTeamValues, null), String.class);
        assertEquals(FORBIDDEN, response.getStatusCode());
        String expectedMessage = "Not allowed to update id on Team. Team id was '1001', id in request body was '2365'";
        assertEquals(expectedMessage, response.getBody());
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

        ResponseEntity<Void> response = restTemplate
                .exchange(createUri(teamResource + teamId + "/users"), PUT, createHttpEntity(userToAdd, null), Void.class);

        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void canAddUserToTeamUsingUserId() {
        Long userId = 1001L;
        given(teamService.addUserToTeam(teamId, userId)).willReturn(mockedTeam);

        JSONObject userToAdd = new JSONObject();
        userToAdd.put("id", "1001");

        ResponseEntity<Void> response = restTemplate
                .exchange(createUri(teamResource + teamId + "/users"), PUT, createHttpEntity(userToAdd, null), Void.class);

        assertEquals(NO_CONTENT, response.getStatusCode());
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

        assertEquals(CREATED, response.getStatusCode());
        String expectedLocation = "/jaxson" + teamResource + teamId;
        assertTrue(response.getHeaders().getLocation().toString().endsWith(expectedLocation));
    }

    @Test
    public void getTeamById() throws URISyntaxException {
        given(teamService.getById(teamId)).willReturn(team);
        ResponseEntity<TeamDto> response = restTemplate.exchange(
                createUri(teamResource + teamId), GET, createHttpEntity(null, null), TeamDto.class);
        assertEquals(OK, response.getStatusCode());
        assertEquals(new TeamDto(team), response.getBody());
    }

    private URI createUri(String resource) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + resource);
        return builder.build().encode().toUri();
    }

    private HttpEntity<?> createHttpEntity(JSONObject body, Map<String, Object> headParameters) {
        HttpHeaders headers = new HttpHeaders();
        if (null != headParameters) headParameters.forEach((k, v) -> headers.set(k, v.toString()));
        headers.set(auth, authCode);
        headers.set("Accept", MediaType.APPLICATION_JSON);
        headers.set("Content-Type", MediaType.APPLICATION_JSON);
        return (null == body) ? new HttpEntity<>(headers) : new HttpEntity<>(body.toString(), headers);
    }

    private static class responseErrorIgnorer implements ResponseErrorHandler {

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