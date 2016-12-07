package se.teknikhogskolan.jaxson.resource.implementation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("issues")
public class IssueResource {

    @GET
    public String getTest(){
        return "Hello world!";
    }
}