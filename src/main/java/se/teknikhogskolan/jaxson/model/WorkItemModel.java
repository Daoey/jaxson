package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;

import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.model.WorkItem.Status;

public final class WorkItemModel extends AbstractEntity {
    
    private String description;

    private Status status = Status.UNSTARTED;

    private LocalDate completionDate;

    private Issue issue;

    private UserModel user;
    
    protected WorkItemModel() {
    }

    public WorkItemModel(WorkItem workItem) {
        
        description = workItem.getDescription();
        completionDate = workItem.getCompletionDate();
        status = workItem.getStatus();
        //user = new UserModel(workItem.getUser());
        //issue = new IssueModel(issue.getIssue());
    }

    public WorkItemModel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public WorkItemModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public WorkItemModel setStatus(Status status) {
        this.status = status;
        return this;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public WorkItemModel setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
        return this;
    }

    public Issue getIssue() {
        return issue;
    }

    public WorkItemModel setIssue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public UserModel getUser() {
        return user;
    }

    public WorkItemModel setUser(UserModel user) {
        this.user = user;
        return this;
    }

    public boolean isDone() {
        if (Status.DONE.ordinal() == this.status.ordinal()) return true;
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorkItemModel other = (WorkItemModel) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        return true;
    }

    public int compareTo(WorkItemModel other) {
        if (null != getId() && null != other.getId()) {
            if (getId() > other.getId()) return 1;
            if (getId() < other.getId()) return -1;
        }
        int result = getDescription().compareTo(other.getDescription());
        if (result < 0) return -1;
        if (result == 0) return 0;
        return 1;
    }
    
    public String completionDateToString() {
        return this.completionDate == null ? "null" : completionDate.toString();
    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("WorkItem [id=");
//        builder.append(getId());
//        builder.append(", description=");
//        builder.append(description);
//        builder.append(", status=");
//        builder.append(status);
//        builder.append(", issueId=");
//        builder.append(issue == null ? "null" : issue.getId());
//        builder.append(", userId=");
//        builder.append(user == null ? "null" : user.getId());
//        builder.append(", created=");
//        builder.append(createdDateToString());
//        builder.append(", lastModified=");
//        builder.append(lastModifiedToString());
//        builder.append(", completionDate=");
//        builder.append(completionDateToString());
//        builder.append("]");
//        return builder.toString();
//    }
    
}
