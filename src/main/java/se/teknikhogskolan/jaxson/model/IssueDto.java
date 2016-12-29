package se.teknikhogskolan.jaxson.model;

import java.time.format.DateTimeFormatter;

import se.teknikhogskolan.springcasemanagement.model.Issue;

public final class IssueDto extends AbstractDto {

    private String description;

    public IssueDto() {
        
    }

    public IssueDto(Issue issue) {
        setId(issue.getId());
        if (null != issue.getCreated()) {
            setCreated(issue.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (null != issue.getLastModified()) {
            setLastModified(issue.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        description = issue.getDescription();
    }

    public String getDescription() {
        return description;
    }

}
