package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import se.teknikhogskolan.jaxson.model.Credentials;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {HsqlInfrastructureConfig.class})
@SqlGroup({@Sql(scripts = "hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
public class TestSecurityResource {

    private static Client client;
    private String baseUrl;
    private Credentials credentials;

    @LocalServerPort
    private int randomPort;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setUp() {
        baseUrl = String.format("http://localhost:%d/jaxson/", randomPort);
        credentials = new Credentials("new user", "pass");
    }

    @Test
    public void canRegisterUser() {
        Response response = client.target(baseUrl).path("register").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(OK, response.getStatusInfo());
    }

    @Test
    public void registerUserShouldReturnToken() {
        Response response = client.target(baseUrl).path("register").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        Token token = response.readEntity(Token.class);
        assertNotNull(token.getToken());
    }

    @Test
    public void registerUserShouldReturnExpirationTime() {
        Response response = client.target(baseUrl).path("register").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        Token token = response.readEntity(Token.class);
        assertNotNull(token.getExpirationTime());
    }

    @Test
    public void shouldReturnBadRequestIfUsernameAlreadyExistWhenRegisterUser() {
        client.target(baseUrl).path("register").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        Response response = client.target(baseUrl).path("register").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void registerUserWithoutPasswordShouldReturnBadRequest() throws Exception {
        Credentials noPassword = new Credentials("username", null);
        Response response = client.target(baseUrl).path("register").request()
                .post(Entity.entity(noPassword, MediaType.APPLICATION_JSON));
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void registerUserWithoutUsernameShouldReturnBadRequest() throws Exception {
        Credentials noUsername = new Credentials(null, "password");
        Response response = client.target(baseUrl).path("register").request()
                .post(Entity.entity(noUsername, MediaType.APPLICATION_JSON));
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }
}
