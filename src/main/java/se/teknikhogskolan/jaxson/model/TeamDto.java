package se.teknikhogskolan.jaxson.model;

import java.util.ArrayList;
import java.util.Collection;

import se.teknikhogskolan.springcasemanagement.model.Team;

public final class TeamDto extends AbstractModel {
    private String name;
    private Collection<Long> usersId;
    private boolean active;

    public TeamDto(String name, Collection<Long> usersId, boolean active) {
        this.name = name;
        this.usersId = usersId;
        this.active = active;
    }

    public TeamDto(){}

    public Collection<Long> setUsersId(Team team) {
        Collection<Long> result = new ArrayList<>();
        if (null != team.getUsers()) {
            team.getUsers().forEach(user -> result.add(user.getId()));
        }
        return result;
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
        final StringBuffer sb = new StringBuffer("TeamDto{");
        sb.append("id=").append(getId());
        sb.append(", name='").append(name).append('\'');
        sb.append(", usersId=").append(usersId);
        sb.append(", active=").append(active);
        sb.append(", created=").append(getCreated());
        sb.append(", lastModified=").append(getLastModified());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TeamDto teamDto = (TeamDto) object;
        return name.equals(teamDto.name);
    }

    @Override
    public int hashCode() {
        int result = 31 + name.hashCode();
        return result;
    }
}
