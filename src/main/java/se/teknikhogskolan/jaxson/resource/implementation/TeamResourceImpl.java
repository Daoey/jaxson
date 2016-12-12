package se.teknikhogskolan.jaxson.resource.implementation;

import static javax.ws.rs.core.Response.Status.CONFLICT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public Response addUserToTeam(Long teamId, Long userId) {
        teamService.addUserToTeam(teamId, userId);
        return Response.accepted().build();
    }

    @Override
    public Response createTeam(TeamDto teamDto) {
        Team team = teamService.create(teamDto.getName());
        updateTeam(teamDto);
        return Response.status(Response.Status.CREATED).header("Location", "teams?id=" + team.getId()).build();
    }

    @Override
    public Response updateTeam(TeamDto newValuesTeamDto) {
        Team team = getTeam(newValuesTeamDto);

        if (!team.isActive() & !newValuesTeamDto.isActive()) {
            throw new ForbiddenOperationException(String.format(
                    "Cannot update Team '%d'. Team is inactive.", team.getId()));
        }

        if (!team.isActive()) {
            teamService.activateTeam(team.getId());
        }
        if (newValuesTeamDto.getName() != team.getName()) {
            teamService.updateName(team.getId(), newValuesTeamDto.getName());
        }
        if (!newValuesTeamDto.isActive()) {
            teamService.inactivateTeam(team.getId());
        }

        return Response.accepted().build();
    }

    private Team getTeam(TeamDto teamDto) {
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
    public Response getTeam(Long id) { // TODO paging if getAll, conflict if getOne
        if (weHave(id)) {
            Team team = teamService.getById(id);
            TeamDto teamDto = new TeamDto(team);
            return Response.ok(teamDto).build();
        }
        Collection<TeamDto> teamDtos = dtosFromTeams(teamService.getAll());
        return Response.ok(teamDtos).build();
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
    public Response getUsersInTeam(Long id, boolean asLocations) {
        List<UserDto> userDtos = new ArrayList<>();
        userService.getAllByTeamId(id).forEach(user -> userDtos.add(new UserDto(user)));
        if (asLocations) {
            List<String> uris = new ArrayList<>();
            userDtos.forEach(u -> uris.add(String.format("../users/%d", u.getId())));
            return Response.ok(uris).build();
        }
        return Response.ok(userDtos).build();
    }
}
