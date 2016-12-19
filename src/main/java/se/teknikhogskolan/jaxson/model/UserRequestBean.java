package se.teknikhogskolan.jaxson.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public final class UserRequestBean {

    @QueryParam("username")
    @DefaultValue("")
    private String username;

    @QueryParam("firstName")
    @DefaultValue("")
    private String firstName;

    @QueryParam("lastName")
    @DefaultValue("")
    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
