package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
@ContextConfiguration(classes = {HsqlInfrastructureConfig.class})
@SqlGroup({@Sql(scripts = "insert_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
public final class TestUserResource {

    private static final String auth = "Authorization";
    private static final String authCode = "Basic cm9vdDpzZWNyZXQ=";
    private static Client client;
    private String baseUrl;
    private User userInDb;
    private User userToCreate;
    @LocalServerPort
    private int randomPort;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setUp() {
        baseUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        userInDb = new User(1L, "Robotarm Luke", "Luke", "Skywalker");
        userToCreate = new User(457L, "canCreateUser", "temp", "temp");
    }

    @Test
    public void canCreateUser() {
        Response response = client.target(baseUrl).path("users").request().header(auth, authCode)
                .post(Entity.entity(userToCreate, MediaType.APPLICATION_JSON));
        assertEquals(CREATED, response.getStatusInfo());
    }

    @Test
    public void creatingUserWithExistingUsernameShouldReturnInternalServerError() throws Exception {
        userToCreate.setUsername(userInDb.getUsername());
        Response response = client.target(baseUrl).path("users").request().header(auth, authCode)
                .post(Entity.entity(userToCreate, MediaType.APPLICATION_JSON));
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusInfo());
    }

    @Test
    public void creatingUserWithExistingUserNumberShouldReturnInternalServerError() throws Exception {
        userToCreate.setUserNumber(userInDb.getUserNumber());
        Response response = client.target(baseUrl).path("users").request().header(auth, authCode)
                .post(Entity.entity(userToCreate, MediaType.APPLICATION_JSON));
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusInfo());
    }

    @Test
    public void canGetUserByUserNumber() {
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode).get();
        UserDto userFromDb = response.readEntity(UserDto.class);
        assertEquals(OK, response.getStatusInfo());
        assertEquals(userInDb, userFromDb);
    }

    @Test
    public void getUserByUserNumberWithoutMatchShouldReturnResponseNotFound() {
        Response response = client.target(baseUrl).path("users/5").request().header(auth, authCode).get();
        assertEquals(NOT_FOUND, response.getStatusInfo());
    }

    @Test
    public void canUpdateUser() {
        userInDb = new User(1L, "New Username", "Luke", "Skywalker");
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(userInDb, MediaType.APPLICATION_JSON));
        assertEquals(NO_CONTENT, response.getStatusInfo());
    }

    @Test
    public void updatingNonExistingUserShouldReturnBadRequest() {
        User nonExistingUser = new User(57L, "Random", "Luke", "Skywalker").setActive(true);
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(nonExistingUser, MediaType.APPLICATION_JSON));
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void canInactiveUser() {
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(userInDb.setActive(false), MediaType.APPLICATION_JSON));
        Response inactivatedResponse = client.target(baseUrl).path("users/1").request().header(auth, authCode).get();
        assertEquals(NO_CONTENT, response.getStatusInfo());
        assertFalse(inactivatedResponse.readEntity(UserDto.class).isActive());
    }

    @Test
    public void updatingInactiveUserWithoutActivatingShouldReturnForbidden() {
        client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(userInDb.setActive(false), MediaType.APPLICATION_JSON));

        userInDb.setUsername("Something new and fresh");
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(userInDb, MediaType.APPLICATION_JSON));
        assertEquals(FORBIDDEN, response.getStatusInfo());
    }


    @Test
    public void canUpdateInactiveUserIfSetToActiveInTheRequest() {
        client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(userInDb.setActive(false), MediaType.APPLICATION_JSON));

        userInDb.setUsername("Something new and fresh");
        Response response = client.target(baseUrl).path("users/1").request().header(auth, authCode)
                .put(Entity.entity(userInDb.setActive(true), MediaType.APPLICATION_JSON));
        assertEquals(NO_CONTENT, response.getStatusInfo());
    }

    @Test
    public void canGetAllUsers() {
        Response response = client.target(baseUrl).path("users").request().header(auth, authCode).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetAllWorkItemsFromUser() {
    /*Response response = client.target(baseUrl).path("users/1/workitems").request().header(auth, authCode).get();
        assertEquals(OK, response.getStatusInfo());*/
    }

    @Test
    public void getAllWorkItemsFromUserWithoutWorkItemsReturnsNotFound() {
        Response response = client.target(baseUrl).path("users/1/workitems").request().header(auth, authCode).get();
        assertEquals(NOT_FOUND, response.getStatusInfo());
    }

    @Test
    public void canAssignWorkItemToUser() {
        // Response response =
        // client.target(baseUrl).path("users/1/workitems").request().put();
    }
}