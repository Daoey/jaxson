package se.teknikhogskolan.jaxson.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("issues")
public interface IssueResource {

    @GET
    String getTest();
}
