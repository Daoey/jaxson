package se.teknikhogskolan.jaxson.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

public abstract class AbstractModel {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Long id;

    private String created;

    private String lastModified;

    public Long getId() {
        return id;
    }

    String getCreated() {
        return created;
    }

    String getLastModified() {
        return lastModified;
    }

    void setId(Long id) {
        this.id = id;
    }

    void setCreated(LocalDate created) {
        this.created = (null == created ? null : created.format(formatter));
    }

    public void setCreated(String created) {
        this.created = created;
    }

    void setLastModified(LocalDate lastModified) {
        this.lastModified = (null == lastModified ? null : lastModified.format(formatter));
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}