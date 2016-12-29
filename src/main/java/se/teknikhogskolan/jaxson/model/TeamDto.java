package se.teknikhogskolan.jaxson.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import se.teknikhogskolan.springcasemanagement.model.Team;

public final class TeamDto extends AbstractDto {

    private String name;

    private Collection<Long> usersId;

    private Boolean active;

    public TeamDto(String name) {
        this.name = name;
    }

    public TeamDto(String name, Collection<Long> usersId, Boolean active) {
        this.name = name;
        this.usersId = usersId;
        this.active = active;
    }

    public TeamDto(Team team) {
        setId(team.getId());
        this.name = team.getName();
        this.active = team.isActive();
        setUsersId(getUsersIdsFromTeam(team));
        if (null != team.getCreated()) {
            setCreated(team.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (null != team.getLastModified()) {
            setLastModified(team.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    public TeamDto(){}

    private Collection<Long> getUsersIdsFromTeam(Team team) {
        Collection<Long> result = new ArrayList<>();
        if (null != team.getUsers()) {
            team.getUsers().forEach(user -> result.add(user.getId()));
        }
        return result;
    }

    private void setUsersId(Collection<Long> usersId) {
        this.usersId = usersId;
    }

    public String getName() {
        return name;
    }

    public Collection<Long> getUsersId() {
        return usersId;
    }

    public Boolean isActive() {
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
        return  31 + name.hashCode();
    }
}