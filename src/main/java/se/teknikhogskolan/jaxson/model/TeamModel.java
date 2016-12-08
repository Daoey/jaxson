package se.teknikhogskolan.jaxson.model;

import se.teknikhogskolan.springcasemanagement.model.Team;

import java.util.ArrayList;
import java.util.Collection;

public final class TeamModel {
    private Long id;
    private String name;
    private Collection<Long> usersId;
    private boolean active;

    public TeamModel(Long id, String name, Collection<Long> usersId, boolean active) {
        this.id = id;
        this.name = name;
        this.usersId = usersId;
        this.active = active;
    }

    public TeamModel (Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.active = team.isActive();
        this.usersId = setUsersId(team);
    }

    public Collection<Long> setUsersId(Team team) {
        Collection<Long> result = new ArrayList<>();
        if (null != team.getUsers()) {
            team.getUsers().forEach(user -> result.add(user.getId()));
        }
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Long> getUsersId() {
        return usersId;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TeamModel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", usersId=").append(usersId);
        sb.append(", active=").append(active);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamModel teamModel = (TeamModel) o;
        return name.equals(teamModel.name);
    }

    @Override
    public int hashCode() {
        int result = 31 + name.hashCode();
        return result;
    }
}
