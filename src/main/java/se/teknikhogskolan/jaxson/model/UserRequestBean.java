package se.teknikhogskolan.jaxson.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.time.LocalDate;

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
}
