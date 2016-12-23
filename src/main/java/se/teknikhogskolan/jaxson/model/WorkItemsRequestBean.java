package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public final class WorkItemsRequestBean {

    public enum RequestType {
        DEFAULT, CONFLICTING, INCOMPLETE, DESCRIPTION, STATUS, CREATED, COMPLETED, HAS_ISSUE
    }

    private @QueryParam("page") @DefaultValue("0") int page;
    private @QueryParam("size") @DefaultValue("10") int size;
    private @QueryParam("description") String description;
    private @QueryParam("status") String status;
    private @QueryParam("createdAfter") String createdAfter;
    private @QueryParam("createdBefore") String createdBefore;
    private @QueryParam("completedAfter") String completedAfter;
    private @QueryParam("completedBefore") String completedBefore;
    private @QueryParam("hasIssue") Boolean hasIssue;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedAfter() {
        try {
            return LocalDate.parse(createdAfter);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(createdAfter
                    + " is not a valid date. Please enter in form: 2007-12-28");
        }
    }

    public LocalDate getCreatedBefore() {
        try {
            return LocalDate.parse(createdBefore);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(createdBefore
                    + " is not a valid date. Please enter in form: 2007-12-28");
        }
    }

    public LocalDate getCompletedAfter() {
        try {
            return LocalDate.parse(completedAfter);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(completedAfter
                    + " is not a valid date. Please enter in form: 2007-12-28");
        }
    }

    public LocalDate getCompletedBefore() {
        try {
            return LocalDate.parse(completedBefore);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(completedBefore
                    + " is not a valid date. Please enter in form: 2007-12-28");
        }
    }

    @SuppressWarnings("cyclomaticcomplexity")
    public RequestType calculateRequestType() {

        RequestType requestType = RequestType.DEFAULT;

        if (description != null) {
            requestType = RequestType.DESCRIPTION;
        }

        if (status != null) {
            if (requestType != RequestType.DEFAULT) {
                return RequestType.CONFLICTING;
            }
            requestType = RequestType.STATUS;
        }

        if (hasIssue != null && Boolean.TRUE.equals(hasIssue)) {

            if (requestType != RequestType.DEFAULT) {
                return RequestType.CONFLICTING;
            }

            requestType = RequestType.HAS_ISSUE;
        }

        if (createdAfter != null || createdBefore != null) {

            if (requestType != RequestType.DEFAULT) {
                return RequestType.CONFLICTING;
            }

            if (createdAfter != null && createdBefore != null) {
                requestType = RequestType.CREATED;
            } else {
                return RequestType.INCOMPLETE;
            }
        }

        if (completedAfter != null || completedBefore != null) {

            if (requestType != RequestType.DEFAULT) {
                return RequestType.CONFLICTING;
            }

            if (completedAfter != null && completedBefore != null) {
                requestType = RequestType.COMPLETED;
            } else {
                return RequestType.INCOMPLETE;
            }
        }

        return requestType;
    }

}
