package se.teknikhogskolan.jaxson.model;

import java.time.format.DateTimeFormatter;

import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.model.WorkItem.Status;

public final class WorkItemDto extends AbstractModel {

    private String description;

    private Status status;

    private String completionDate;

    private Long issueId;

    private Long userId;

    private Long userNumber;

    protected WorkItemDto() {
    }

    public WorkItemDto(WorkItem workItem) {
        
        setId(workItem.getId());
        setCreated(workItem.getCreated());
        setLastModified(workItem.getLastModified());
        description = workItem.getDescription();
        status = workItem.getStatus();

        if (workItem.getCompletionDate() != null) {
            completionDate = workItem.getCompletionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (workItem.getUser() != null) {
            userId = workItem.getUser().getId();
            userNumber = workItem.getUser().getUserNumber();
        }

        if (workItem.getIssue() != null) {
            issueId = workItem.getIssue().getId();
        }
    }

    public WorkItemDto(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public Long getIssueId() {
        return issueId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    @Override
    public String toString() {
        return "WorkItemDto [description=" + description + ", status=" + status + ", completionDate=" + completionDate
                + ", issueId=" + issueId + ", userId=" + userId + ", userNumber=" + userNumber + "]";
    }

    
    
}
