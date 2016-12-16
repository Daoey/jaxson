package se.teknikhogskolan.jaxson.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public final class PageRequestBean {

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
}
