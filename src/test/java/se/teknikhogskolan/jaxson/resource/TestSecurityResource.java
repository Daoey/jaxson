package se.teknikhogskolan.jaxson.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
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
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {HsqlInfrastructureConfig.class})
@SqlGroup({@Sql(scripts = "hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
public final class TestSecurityResource {

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
        Response response = registerUser();
        assertEquals(OK, response.getStatusInfo());
    }

    @Test
    public void registerUserShouldReturnBothAuthAndRefreshToken() {
        Response response = registerUser();
        Map<String, Token> result = response.readEntity(new GenericType<Map<String, Token>>(){});
        assertTrue(result.containsKey("authorization token"));
        assertTrue(result.containsKey("refresh token"));
    }

    @Test
    public void registerUserShouldReturnExpirationTime() {
        Response response = registerUser();
        Map<String, Token> result = response.readEntity(new GenericType<Map<String, Token>>(){});
        assertTrue(result.containsKey("authorization token"));
        assertTrue(result.containsKey("refresh token"));
    }

    @Test
    public void shouldReturnBadRequestIfUsernameAlreadyExistWhenRegisterUser() {
        registerUser();
        Response response = registerUser();
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void registerUserWithoutPasswordShouldReturnBadRequest() {
        this.credentials = new Credentials("username", null);
        Response response = registerUser();
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void registerUserWithoutUsernameShouldReturnBadRequest() {
        this.credentials = new Credentials(null, "password");
        Response response = registerUser();
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void canLogin() {
        registerUser();
        Response response = loginUser();
        assertEquals(OK, response.getStatusInfo());
    }

    @Test
    public void loginUserShouldReturnTokens() {
        registerUser();
        Response response = loginUser();
        Map<String, Token> result = response.readEntity(new GenericType<Map<String, Token>>(){});
        assertNotNull(result.get("refresh token").getToken());
        assertNotNull(result.get("authorization token").getToken());
    }

    @Test
    public void loginUserShouldReturnExpirationTime() {
        registerUser();
        Response response = loginUser();
        Map<String, Token> result = response.readEntity(new GenericType<Map<String, Token>>(){});
        assertNotNull(result.get("authorization token").getExpirationTime());
        assertNotNull(result.get("refresh token").getExpirationTime());
    }

    @Test
    public void shouldReturnBadRequestIfUsernameDoNotExist() {
        registerUser();
        this.credentials = new Credentials("wrong", "wrong");
        Response response = loginUser();
        assertEquals(NOT_FOUND, response.getStatusInfo());
        System.out.println(response.getEntity().toString());
    }

    @Test
    public void shouldReturnUnAuthorizedIfPasswordIsWrong() {
        registerUser();
        String correctUsername = credentials.getUsername();
        this.credentials = new Credentials(correctUsername, "Wrong");
        Response response = loginUser();
        assertEquals(UNAUTHORIZED, response.getStatusInfo());
    }

    @Test
    public void loginShouldReturnBadRequestIfPasswordIsNull() {
        registerUser();
        String correctUsername = credentials.getUsername();
        this.credentials = new Credentials(correctUsername, null);
        Response response = loginUser();
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void loginShouldReturnBadRequestIfUsernameIsNull() {
        registerUser();
        String correctPassword = credentials.getPassword();
        this.credentials = new Credentials(null, correctPassword);
        Response response = loginUser();
        assertEquals(BAD_REQUEST, response.getStatusInfo());
    }

    private Response registerUser() {
        return client.target(baseUrl).path("register").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
    }

    private Response loginUser() {
        return client.target(baseUrl).path("login").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
    }
}
