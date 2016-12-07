package se.teknikhogskolan.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

public abstract class AbstractEntity {

    private Long id;

    private LocalDate created;

    private LocalDate lastModified;

    public Long getId() {
        return id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }
    
    public String lastModifiedToString() {
        return this.lastModified == null ? "null" : lastModified.toString();
    }
    
    public String createdDateToString() {
        return this.created == null ? "null" : created.toString();
    }
}