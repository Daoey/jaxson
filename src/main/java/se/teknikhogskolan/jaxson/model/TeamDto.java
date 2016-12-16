package se.teknikhogskolan.jaxson.model;

import java.util.ArrayList;
import java.util.Collection;

import se.teknikhogskolan.springcasemanagement.model.Team;

public final class TeamDto extends AbstractModel {

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
        setUsersId(team);
        setCreated(team.getCreated());
        setLastModified(team.getLastModified());
    }

    public TeamDto(){}

    public void setActive(Boolean active) {
        this.active = (null == active) ? true : active;
    }

    private void setUsersId(Object object) {
        if (setUsersIdInvokedByTeamConstructor(object)) {
            handleAsTeam(object); // Team has Collection<User>, User has Long id
        }
        if (setUsersIdInvokedByJaxRs(object)) {
            handleAsCollectionOfLong(object); // Collection<Long> is collection of id
        }
    }

    private boolean setUsersIdInvokedByTeamConstructor(Object object) {
        return object instanceof Team;
    }

    private void handleAsTeam(Object object) {
        Team team = (Team) object;
        Collection<Long> result = new ArrayList<>();
        if (null != team.getUsers()) {
            team.getUsers().forEach(user -> result.add(user.getId()));
        }
        this.usersId = result;
    }

    private boolean setUsersIdInvokedByJaxRs(Object object) {
        return object instanceof Collection;
    }

    private void handleAsCollectionOfLong(Object object) {
        this.usersId = (Collection<Long>) object;
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