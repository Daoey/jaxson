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
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.jaxson.resource.TeamResource;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;
import se.teknikhogskolan.springcasemanagement.service.exception.NotAllowedException;

public final class TeamResourceImpl implements TeamResource {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkItemService workItemService;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response createTeam(TeamDto teamDto) {
        if (usersIn(teamDto)) { // TODO move to filter(?)
            throw new ForbiddenOperationException(
                    "Cannot create Team with Users. Create Team here, then PUT User to ../teams/{teamId}/users");
        }
        if (null != teamDto.getId()) { // TODO move to filter(?)
            throw new ForbiddenOperationException(String.format( // TODO Change to 412?
                    "Cannot create Team, Team with id '%d' already exist. To update Team use PUT to ../teams/{teamId}" ,
                    teamDto.getId()));
        }
        Team team = teamService.create(teamDto.getName());

        if (inactiveStatusIsRequested(teamDto)) {
            teamService.inactivateTeam(team.getId());
        }

        URI location = uriInfo.getAbsolutePathBuilder().path(team.getId().toString()).build();
        return Response.created(location).build();
    }

    private boolean usersIn(TeamDto teamDto) {
        return !(null == teamDto.getUsersId() || teamDto.getUsersId().isEmpty());
    }

    private boolean inactiveStatusIsRequested(TeamDto teamDto) {
        return null != teamDto.isActive() && !teamDto.isActive();
    }

    @Override
    public Response updateTeam(Long id, TeamDto newValuesTeamDto) {

        stopAttemptsToUpdateTeamId(id, newValuesTeamDto); // TODO move to filter(?)

        Team teamModel = getTeamModelById(id);

        stopAttemptsToUpdateInactiveTeam(teamModel, newValuesTeamDto); // TODO move to filter(?)

        update(teamModel, newValuesTeamDto);

        return Response.noContent().build();
    }

    private void stopAttemptsToUpdateTeamId(Long id, TeamDto newValuesTeamDto) {
        if (idInTeamDtoDoNotMatchPathParamId(id, newValuesTeamDto)) {
            throw new ForbiddenOperationException(String.format(
                    "Not allowed to update id on Team. Team id was '%d', id in request body was '%d'",
                    id, newValuesTeamDto.getId()));
        }
    }

    private boolean idInTeamDtoDoNotMatchPathParamId(Long id, TeamDto newValuesTeamDto) {
        return null != newValuesTeamDto.getId() & !id.equals(newValuesTeamDto.getId());
    }

    private void stopAttemptsToUpdateInactiveTeam(Team teamModel, TeamDto newValuesTeamDto) {
        if (inactiveAndNotUpdatedToActive(teamModel, newValuesTeamDto)) {
            throw new ForbiddenOperationException(String.format(
                    "Not allowed to update inactive Team. Team with id '%d' is inactive", teamModel.getId()));
        }
    }

    private Team getTeamModelById(Long id) {
        Team team = teamService.getById(id);
        if (notNullOrEmpty(team)) {
            return team;
        } else throw new NotFoundException(String.format("Cannot find Team with id '%d'", id));
    }

    private boolean inactiveAndNotUpdatedToActive(Team team, TeamDto newValuesTeamDto) {
        if (!team.isActive()) {
            if (null == newValuesTeamDto.isActive() || !newValuesTeamDto.isActive()) {
                return true;
            }
        }
        return false;
    }

    private void update(Team team, TeamDto newValuesTeamDto) {
        if (activationNeededToMakeUpdatePossibleOn(team)) {
            makeUpdatable(team);
        }
        if (nameUpdateIsRequested(team, newValuesTeamDto)) {
            teamService.updateName(team.getId(), newValuesTeamDto.getName());
        }
        if (inactiveIsTheWantedStatus(newValuesTeamDto)) {
            teamService.inactivateTeam(team.getId());
        }
    }

    private boolean activationNeededToMakeUpdatePossibleOn(Team team) {
        return !team.isActive();
    }

    private void makeUpdatable(Team team) {
        teamService.activateTeam(team.getId());
    }

    private boolean nameUpdateIsRequested(Team team, TeamDto newValuesTeamDto) {
        return newValuesTeamDto.getName() != team.getName();
    }

    private boolean inactiveIsTheWantedStatus(TeamDto newValuesTeamDto) {
        return (null == newValuesTeamDto.isActive()) ? false : !newValuesTeamDto.isActive();
    }

    private boolean notNullOrEmpty(Object object) {
        if (null == object) return false;
        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            if (collection.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public Collection<TeamDto> getTeams() {
        return dtosFromTeams(teamService.getAll());
    }

    private Collection<TeamDto> dtosFromTeams(Iterable<Team> teams) {
        Collection<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(team -> teamDtos.add(new TeamDto(team)));
        return teamDtos;
    }

    @Override
    public TeamDto getTeam(Long id) {
        return new TeamDto(teamService.getById(id));
    }

    @Override
    public Response addUserToTeam(Long teamId, UserDto userDto) {
        Long userId = userDto.getId();
        Long userNumber = userDto.getUserNumber();

        if (notNullOrEmpty(userId) && notNullOrEmpty(teamId)) {

            teamService.addUserToTeam(teamId, userId);
            return Response.noContent().build();

        } else if (notNullOrEmpty(userNumber) && notNullOrEmpty(teamId)) {

            User user = userService.getByUserNumber(userNumber);
            teamService.addUserToTeam(teamId, user.getId());
            return Response.noContent().build();

        } else throw new IncompleteException(String.format("No User id or User number found in request."));
    }

    @Override
    public Collection<UserDto> getUsersInTeam(Long id) {
        Collection<UserDto> userDtos = new ArrayList<>();
        userService.getAllByTeamId(id).forEach(user -> userDtos.add(new UserDto(user)));
        return userDtos;
    }

    @Override
    public Collection<WorkItemDto> getWorkItemsInTeam(Long id) {
        Collection<WorkItemDto> workItemDtos = dtosFromWorkItems(workItemService.getByTeamId(id));
        return workItemDtos;
    }

    private Collection<WorkItemDto> dtosFromWorkItems(Collection<WorkItem> workItems) {
        Collection<WorkItemDto> dtos = new ArrayList<>();
        workItems.forEach(workItem -> dtos.add(new WorkItemDto(workItem)));
        return dtos;
    }
}