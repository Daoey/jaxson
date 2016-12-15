package se.teknikhogskolan.jaxson.resource.implementation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.exception.ForbiddenOperationException;
import se.teknikhogskolan.jaxson.exception.IncompleteException;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.resource.TeamResource;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

public class TeamResourceImpl implements TeamResource {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response addUserToTeam(Long teamId, UserDto userDto) {
        Long userId = userDto.getId();
        Long userNumber = userDto.getUserNumber();

        if (weHave(userId) && weHave(teamId)) {

            teamService.addUserToTeam(teamId, userId);
            return Response.accepted().build();

        } else if (weHave(userNumber) && weHave(teamId)) {

            User user = userService.getByUserNumber(userNumber);
            teamService.addUserToTeam(teamId, user.getId());
            return Response.accepted().build();

        } else throw new IncompleteException(String.format("No user id or user number found in request."));
    }

    @Override
    public Response createTeam(TeamDto teamDto) {
        if (usersIn(teamDto)) {
            throw new ForbiddenOperationException(
                    "Cannot create Team with Users. Create Team here, then PUT User to ../teams/{teamId}");
        }
        Team team = teamService.create(teamDto.getName());
        if (null != teamDto.isActive() && !teamDto.isActive()) teamService.inactivateTeam(team.getId());

        URI location = uriInfo.getAbsolutePathBuilder().path(team.getId().toString()).build();
        return Response.created(location).build();
    }

    private boolean usersIn(TeamDto teamDto) {
        return !(null == teamDto.getUsersId() || teamDto.getUsersId().isEmpty());
    }

    @Override
    public Response updateTeam(TeamDto newValuesTeamDto) {

        Team team = getAsTeam(newValuesTeamDto);

        if (!team.isActive() & (!newValuesTeamDto.isActive())) {
            throw new ForbiddenOperationException(String.format(
                    "Cannot update Team with id '%d'. Team is inactive.", team.getId()));
        } else {
            update(team, newValuesTeamDto);
            return Response.accepted().build();
        }
    }

    private void update(Team team, TeamDto newValuesTeamDto) {
        if (activationNeededToUpdate(team)) {
            teamService.activateTeam(team.getId());
        }
        if (nameNeedsToBeSyncedBetween(team, newValuesTeamDto)) {
            teamService.updateName(team.getId(), newValuesTeamDto.getName());
        }
        if (activeStatusDiffersFrom(newValuesTeamDto)) {
            teamService.inactivateTeam(team.getId());
        }
    }

    private boolean activationNeededToUpdate(Team team) {
        return !team.isActive();
    }

    private boolean nameNeedsToBeSyncedBetween(Team team, TeamDto newValuesTeamDto) {
        return newValuesTeamDto.getName() != team.getName();
    }

    private boolean activeStatusDiffersFrom(TeamDto newValuesTeamDto) {
        return !newValuesTeamDto.isActive();
    }

    private Team getAsTeam(TeamDto teamDto) {
        Team team;
        if (weHave(teamDto.getId())) {
            team = teamService.getById(teamDto.getId());
        } else if (weHave(teamDto.getName())) {
            team = teamService.getByName(teamDto.getName());
        } else throw new IncompleteException(String.format(
                "Team id or Team name needed to locate Team. Id was '%d' and name was '%s'",
                teamDto.getId(), teamDto.getName()));

        if (weHave(team)) {
            return team;
        } else throw new NotFoundException(String.format(
                "Cannot find Team with id '%d' or name '%s'", teamDto.getId(), teamDto.getName()));
    }

    @Override
    public Collection<TeamDto> getTeams() {
        return dtosFromTeams(teamService.getAll());
    }

    @Override
    public TeamDto getTeam(Long id) {
        return new TeamDto(teamService.getById(id));
    }

    private Collection<TeamDto> dtosFromTeams(Iterable<Team> teams) {
        Collection<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(team -> teamDtos.add(new TeamDto(team)));
        return teamDtos;
    }

    private boolean weHave(Object object) {
        if (null == object) return false;
        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            if (collection.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public Collection<UserDto> getUsersInTeam(Long id) {
        Collection<UserDto> userDtos = new ArrayList<>();
        userService.getAllByTeamId(id).forEach(user -> userDtos.add(new UserDto(user)));
        return userDtos;
    }
}