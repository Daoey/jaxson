package se.teknikhogskolan.jaxson.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public final class UserRequestBean {

    @QueryParam("username")
    @DefaultValue("")
    private String username;

    @QueryParam("firstname")
    @DefaultValue("")
    private String firtname;

    @QueryParam("lastname")
    @DefaultValue("")
    private String lastname;

    @QueryParam("page")
    private int page;

    @QueryParam("size")
    @DefaultValue("10")
    private int size;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

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
