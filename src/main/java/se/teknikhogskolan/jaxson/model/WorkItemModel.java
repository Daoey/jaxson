package se.teknikhogskolan.jaxson.model;

import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.model.WorkItem.Status;

public final class WorkItemModel extends AbstractModel {
    
    private String description;

    private Status status = Status.UNSTARTED;

    private String completionDate;

    private Long issueId;

    private Long userId;

    protected WorkItemModel() {}

    public WorkItemModel(WorkItem workItem) {
        description = workItem.getDescription();
        completionDate = workItem.getCompletionDate().format(formatter);
        status = workItem.getStatus();
        userId = (null == workItem.getUser() ? null : workItem.getUser().getId());
        issueId = (null == workItem.getIssue() ? null : workItem.getIssue().getId());
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

    public String getCompletionDate() {
        return completionDate;
    }

    public Long getIssueId() {
        return issueId;
    }

    public Long getUserId() {
        return userId;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WorkItem [id=");
        builder.append(getId());
        builder.append(", description=");
        builder.append(description);
        builder.append(", status=");
        builder.append(status);
        builder.append(", issueId=");
        builder.append(issueId);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", created=");
        builder.append(getCreated());
        builder.append(", lastModified=");
        builder.append(getLastModified());
        builder.append(", completionDate=");
        builder.append(completionDate);
        builder.append("]");
        return builder.toString();
    }
}
