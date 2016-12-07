package se.teknikhogskolan.jaxson.model;

import java.util.List;

public final class TeamModel {
    private Long id;
    private String name;
    private List<Long> users;
    private boolean active;

    public TeamModel(Long id, String name, List<Long> users, boolean active) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getUsers() {
        return users;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TeamModel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", users=").append(users);
        sb.append(", active=").append(active);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamModel teamModel = (TeamModel) o;

        if (!id.equals(teamModel.id)) return false;
        return name.equals(teamModel.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
