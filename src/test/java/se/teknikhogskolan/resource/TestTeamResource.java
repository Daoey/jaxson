package se.teknikhogskolan.resource;

import org.junit.BeforeClass;
import org.junit.Test;
import se.teknikhogskolan.model.TeamViewBean;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class TestTeamResource {
    private static Client client;
    private static final String webAppUrl = "http://localhost:8080/jaxson";
    private static final String resource = "teams";

    @BeforeClass
    public static void masterSetup() {
        client = ClientBuilder.newClient();
    }

    @Test
    public void canGetTeamById() {
        Long teamId = 14L;
        WebTarget target = client.target(webAppUrl).path(resource).path(teamId.toString());

        TeamViewBean teamViewBean = target.request(MediaType.APPLICATION_JSON_TYPE).get(TeamViewBean.class);

        System.out.println(teamViewBean);
        assertEquals(teamId, teamViewBean.getId());
    }
}
