package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class AbstractModel {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
    
    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    void setId(Long id) {
        this.id = id;
    }

    public void setCreated(LocalDate created) {
        this.created = (null == created ? null : created.format(formatter));
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = (null == lastModified ? null : lastModified.format(formatter));
    }
}