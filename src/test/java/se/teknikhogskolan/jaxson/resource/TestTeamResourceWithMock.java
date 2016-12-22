package se.teknikhogskolan.jaxson.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.teknikhogskolan.jaxson.JaxsonApplication;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.TeamViewBean;
import se.teknikhogskolan.jaxson.resource.implementation.TeamResourceImpl;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.service.TeamService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.HttpMethod.GET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTeamResourceWithMock {

    @MockBean
    TeamService teamService;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int randomPort;

    @Test
    public void testMocking() throws URISyntaxException {
        Long teamId = 1001L;
        String teamName = "Mocking Team";
        Team team = new Team(teamName);
        given(this.teamService.getById(teamId)).willReturn(team);

        RestTemplate restTemplate = restTemplateBuilder.build();
        Map<String, String> params = new HashMap<>();
        params.put("id", teamId.toString());
        ResponseEntity<TeamDto> response = restTemplate.getForEntity("http://localhost:" + randomPort + "/jaxson/teams/{id}", TeamDto.class, params);

        assertEquals(OK, response.getStatusCode());
        assertEquals(new TeamDto(team), response.getBody());
    }

}