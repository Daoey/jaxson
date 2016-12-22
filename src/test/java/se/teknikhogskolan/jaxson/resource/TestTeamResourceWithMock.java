package se.teknikhogskolan.jaxson.resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTeamResourceWithMock {

    @MockBean
    private TeamService teamService;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int randomPort;

    private static String baseUrl;
    private final String auth = "Authorization";
    private final String authCode = "Basic cm9vdDpzZWNyZXQ=";
    private final Long teamId = 1001L;
    private final String teamName = "Mocking Team";

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + randomPort + "/jaxson";
    }

    @Test
    public void testMockingWithAuth() throws URISyntaxException {
        Team team = new Team(teamName);
        given(this.teamService.getById(teamId)).willReturn(team);

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<TeamDto> response = restTemplate.exchange(createUri("/teams/1001"), HttpMethod.GET, getHttpEntity(), TeamDto.class);

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