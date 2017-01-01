package se.teknikhogskolan.jaxson.resource;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.SyncInvoker;
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
    @LocalServerPort
    private int randomPort;
    private String baseUrl;
    private static UserDto userInDb;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
        userInDb = new UserDto(new User(1L, "Robotarm Luke", "Luke", "Skywalker"));
    }

    @Before
    public void setUp() {
        baseUrl = "http://localhost:" + randomPort + "/jaxson/";
    }

    @Test
    public void canCreateUser() throws Exception {
        Response response = client.target(baseUrl).path("users").request()
                .post(Entity.entity(new User(457L, "canCreateUser", "temp", "temp"), MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void canGetUserByUserNumber() {
        Response response = client.target(baseUrl).path("users/1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserDto userFromDb = response.readEntity(UserDto.class);
        System.out.println(userFromDb);
        assertEquals(userInDb, userFromDb);
    }

    @Test
    public void canUpdateUser() {
        // Response response =
        // client.target(baseUrl).path("users/1").request().put();
    }

    @Test
    public void canGetAllUsers() {
        Response response = client.target(baseUrl).path("users").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }


    @Test
    public void canGetAllWorkItemsFromUser() {
        Response response = client.target(baseUrl).path("users/1/workitems").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void canAssignWorkItemToUser() {
        // Response response =
        // client.target(baseUrl).path("users/1/workitems").request().put();
    }
}