package se.teknikhogskolan.model;

import java.util.ArrayList;
import java.util.Collection;

public final class TeamViewBean {

    private Long id;
    private String created;
    private String lastModified;

    private String name;
    private boolean active;
    private Collection<Long> usersId;

    public TeamViewBean() {}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TeamViewBean{");
        sb.append("id=").append(id);
        sb.append(", created='").append(created).append('\'');
        sb.append(", lastModified='").append(lastModified).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", active=").append(active);
        sb.append(", usersId=").append(usersId);
        sb.append('}');
        return sb.toString();
    }

    public Long getId() {
        return new Long(id);
    }

    public String getLastModified() {
        return new String(lastModified);
    }

    public String getCreated() {
        return new String(created);
    }

    public String getName() {
        return new String(name);
    }

    public boolean isActive() {
        return active;
    }

    public Collection<Long> getUsersId() {
        return new ArrayList<>(usersId);
    }
}