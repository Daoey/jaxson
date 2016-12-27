package se.teknikhogskolan.jaxson.resource;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.service.TeamService;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTeamResourceWithMock {

    @MockBean
    private TeamService teamService;

    @Mock
    Team mockedTeam;

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
    public void createTeam() throws URISyntaxException {
        given(teamService.create(mockedTeam.getName())).willReturn(mockedTeam);
        when(mockedTeam.getId()).thenReturn(teamId);

        JSONObject request = new JSONObject();
        request.put("name", mockedTeam.getName());
        request.put("active", true);

        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, authCode);
        headers.set("Content-Type", MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restTemplate
                .exchange(createUri(teamResource), HttpMethod.POST, entity, String.class);

        assertEquals(CREATED, response.getStatusCode());
        String expectedLocation = "/jaxson" + teamResource + teamId;
        assertTrue(response.getHeaders().getLocation().toString().endsWith(expectedLocation));
    }

    @Test
    public void getTeamById() throws URISyntaxException {
        given(teamService.getById(teamId)).willReturn(team);

        ResponseEntity<TeamDto> response = restTemplate.exchange(createUri(teamResource + teamId), HttpMethod.GET, getHttpEntity(), TeamDto.class);
        assertEquals(OK, response.getStatusCode());
        assertEquals(new TeamDto(team), response.getBody());
    }

    private URI createUri(String resource) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + resource);
        return builder.build().encode().toUri();
    }

    private HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(auth, authCode);
        headers.set("Accept", MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(headers);
    }
}