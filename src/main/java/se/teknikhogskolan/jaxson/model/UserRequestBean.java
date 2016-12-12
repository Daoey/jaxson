package se.teknikhogskolan.jaxson.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class UserRequestBean {

    @QueryParam("username")
    @DefaultValue("")
    private String username;

    @QueryParam("firstname")
    @DefaultValue("")
    private String firtname;

    @QueryParam("lastname")
    @DefaultValue("")
    private String lastname;

    public String getUsername() {
        return username;
    }

    public String getFirtname() {
        return firtname;
    }

    public String getLastname() {
        return lastname;
    }
}
