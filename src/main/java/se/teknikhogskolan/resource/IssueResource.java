package se.teknikhogskolan.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("issues")
public class IssueResource {

    @GET
    public String getTest(){
        return "Hello world!";
    }
}