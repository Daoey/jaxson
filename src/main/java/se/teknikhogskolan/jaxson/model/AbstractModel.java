package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class AbstractModel {

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

    public void setCreated(LocalDate created) {
        this.created = (null == created ? null : created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = (null == lastModified ? null
                : lastModified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}