package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;

import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.model.WorkItem.Status;

public final class WorkItemModel extends AbstractModel {

    private String description;

    private Status status;

    private LocalDate completionDate;

    private Issue issue;

    private Long userId;
    
    private Long userNumber;

    protected WorkItemModel() {
    }

    public WorkItemModel(WorkItem workItem) {
        setId(workItem.getId());
        setCreated(workItem.getCreated());
        setLastModified(workItem.getLastModified());
        description = workItem.getDescription();
        completionDate = workItem.getCompletionDate();
        status = workItem.getStatus();
        if (workItem.getUser() != null) {
            userId = workItem.getUser().getId();
            userNumber = workItem.getUser().getUserNumber();
        }
        // issue = new IssueModel(issue.getIssue());
    }

    public WorkItemModel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public Issue getIssue() {
        return issue;
    }

    public Long getUserId() {
        return userId;
    }
    
    public Long getUserNumber() {
        return userNumber;
    }

}
