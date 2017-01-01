package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;
import se.teknikhogskolan.springcasemanagement.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HsqlInfrastructureConfig.class })
@SqlGroup({ @Sql(scripts = "insert_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public final class TestUserResource {

    private static Client client;
    private String baseUrl;
    private UserDto userInDb;
    private static final String auth = "Authorization";
    private static final String authCode = "Basic cm9vdDpzZWNyZXQ=";
    @LocalServerPort
    private int randomPort;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setUp() {
        baseUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        userInDb = new UserDto(new User(1L, "Robotarm Luke", "Luke", "Skywalker"));
    }

    @Test
    public void canCreateUser() throws Exception {
        Response response = client.target(baseUrl).path("users").request().header(auth, authCode)
                .post(Entity.entity(new User(457L, "canCreateUser", "temp", "temp"), MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetAllUsers() {
        Response response = client.target(baseUrl).path("users").request().header(auth, authCode).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserByUserNumberWithoutMatchShouldReturnResponseNotFound() {
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode).get();
        assertEquals(NOT_FOUND, response.getStatusInfo());
    }

    @Test
    public void canUpdateUser() {
        // TODO implement canUpdateUser test
        // Response response =
        // client.target(baseUrl).path("users/1").request().put();
    }



    @Test
    public void getAllWorkItemsFromUserWithoutWorkItemsReturnsNotFound() {
        Response response = client.target(baseUrl).path("users/1/workitems").request().header(auth, authCode).get();
        assertEquals(NOT_FOUND, response.getStatusInfo());
    }

    @Test
    public void canAssignWorkItemToUser() {
        // TODO implement canAssignWorkItemToUser test
        // Response response =
        // client.target(baseUrl).path("users/1/workitems").request().put();
    }
}