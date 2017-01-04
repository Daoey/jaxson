package se.teknikhogskolan.jaxson.resource;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import se.teknikhogskolan.jaxson.JaxsonApplication;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxsonApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { HsqlInfrastructureConfig.class })
@SqlGroup({ @Sql(scripts = "add_workitem_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "hsql_clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class TestWorkItemResource {

    private static Client client;
    private static final String AUTH = "Authorization";
    private static final String AUTH_CODE = "Basic cm9vdDpzZWNyZXQ=";
    private static final String WORKITEMS = "workitems";
    private static final String WORKITEM_IN_DATABASE = "workitems/98481111";
    private static final String WORKITEM_TO_BE_DELETED = "workitems/98422222";
    private static final String NON_EXISTENT_WORKITEM = "workitems/99999999";
    private String baseUrl;

    @LocalServerPort
    private int randomPort;

    @BeforeClass
    public static void initialize() {
        client = ClientBuilder.newClient();
    }

    @Before
    public void setup() {
        baseUrl = String.format("http://localhost:%d/jaxson/", randomPort);
    }

    @Test
    public void createWorkItemShouldReturnCreated() {
        Response result = client.target(baseUrl).path(WORKITEMS).request().header(AUTH, AUTH_CODE)
                .post(Entity.entity(new WorkItemDto("some description"), MediaType.APPLICATION_JSON));
        assertEquals(Status.CREATED, result.getStatusInfo());
    }

    @Test
    public void createWorkItemNoJsonBodyShouldReturnBadRequest() {
        Response result = client.target(baseUrl).path(WORKITEMS).request().header(AUTH, AUTH_CODE)
                .post(Entity.json(null));
        assertEquals(Status.BAD_REQUEST, result.getStatusInfo());
    }

    @Test
    public void getWorkItemShouldReturnOk() {
        Response result = client.target(baseUrl).path(WORKITEM_IN_DATABASE).request().header(AUTH, AUTH_CODE).get();
        assertEquals(Status.OK, result.getStatusInfo());
    }

    @Test
    public void getWorkItemThatDoesNotExistShouldReturnNotFound() {
        Response result = client.target(baseUrl).path(NON_EXISTENT_WORKITEM).request().header(AUTH, AUTH_CODE).get();
        assertEquals(Status.NOT_FOUND, result.getStatusInfo());
    }

    @Test
    public void updateStatusShouldReturnNoContent() {
        WorkItem workItem = new WorkItem("some description");
        workItem.setStatus(se.teknikhogskolan.springcasemanagement.model.WorkItem.Status.DONE);
        WorkItemDto workItemDto = new WorkItemDto(workItem);
        Response result = client.target(baseUrl).path(WORKITEM_IN_DATABASE).request().header(AUTH, AUTH_CODE)
                .put(Entity.entity(workItemDto, MediaType.APPLICATION_JSON));
        assertEquals(Status.NO_CONTENT, result.getStatusInfo());
    }

    @Test
    public void updateStatusNoJsonStatusShouldReturnBadRequest() {
        WorkItemDto workItemDto = new WorkItemDto("some description");
        Response result = client.target(baseUrl).path(WORKITEM_IN_DATABASE).request().header(AUTH, AUTH_CODE)
                .put(Entity.entity(workItemDto, MediaType.APPLICATION_JSON));
        assertEquals(Status.BAD_REQUEST, result.getStatusInfo());
    }

    @Test
    public void deleteWorkItemShouldReturnNoContent() {
        Response result = client.target(baseUrl).path(WORKITEM_TO_BE_DELETED).request().header(AUTH, AUTH_CODE)
                .delete();
        assertEquals(Status.NO_CONTENT, result.getStatusInfo());
    }

}