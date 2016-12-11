package se.teknikhogskolan.jaxson.model;

import se.teknikhogskolan.springcasemanagement.model.Issue;

public final class IssueDto extends AbstractModel {

    private String description;

    public IssueDto() {
        
    }

    public IssueDto(Issue issue) {
        setId(issue.getId());
        setCreated(issue.getCreated());
        setLastModified(issue.getLastModified());
        description = issue.getDescription();
    }

    public String getDescription() {
        return description;
    }

}
