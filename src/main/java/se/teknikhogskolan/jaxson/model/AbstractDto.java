package se.teknikhogskolan.jaxson.model;

public abstract class AbstractDto {

    private Long id;

    private String created;

    private String lastModified;

    public Long getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getLastModified() {
        return lastModified;
    }

    void setId(Long id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}