package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public final class DateRequestBean {

    @QueryParam("startDate")
    @DefaultValue("2016-01-01")
    private LocalDate startDate;

    @QueryParam("endDate")
    @DefaultValue("2017-01-01")
    private LocalDate endDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
