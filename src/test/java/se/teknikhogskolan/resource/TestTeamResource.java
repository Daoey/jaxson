package se.teknikhogskolan.resource;

import org.junit.BeforeClass;
import org.junit.Test;
import se.teknikhogskolan.model.TeamViewBean;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestTeamResource {
    private static WebTarget teamWebTarget;

    @BeforeClass
    public static void masterSetup() {
        String webAppUrl = "http://localhost:8080/jaxson";
        String resource = "teams";
        teamWebTarget = ClientBuilder.newClient().target(webAppUrl).path(resource);
    }

    @Test
    public void canGetTeamById() {
        Long teamId = 14L;
        Response response = teamWebTarget.path(teamId.toString()).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(OK, response.getStatusInfo());
        TeamViewBean teamViewBean = response.readEntity(TeamViewBean.class);
        assertEquals(teamId, teamViewBean.getId());
        assertNotEmpty(teamViewBean.getName());
    }

    private static void assertNotEmpty(String string) {
        if (null == string || string.isEmpty()) fail();
    }
}
