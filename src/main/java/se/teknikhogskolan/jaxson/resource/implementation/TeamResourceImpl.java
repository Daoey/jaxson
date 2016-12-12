package se.teknikhogskolan.jaxson.resource.implementation;


import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelFrom;
import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelsFromTeams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

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
        return Response.status(Response.Status.CREATED).header("Location", "teams?id=" + teamDto.getId()).build();
    }

    @Override
    public Response getTeam(Long id) {
        if (weHave(id)) {
            Team team = teamService.getById(id);
            TeamDto teamDto = teamModelFrom(team);
            return Response.ok(teamDto).build();
        }
        Collection<TeamDto> teamDtos = teamModelsFromTeams(teamService.getAll());
        return Response.ok(teamDtos).build();
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

    @Override
    public Response updateTeam(TeamDto teamDto) {

        Team team;
        if (weHave(teamDto.getId())) {
            team = teamService.getById(teamDto.getId());
        } else if (weHave(teamDto.getName())) {
            team = teamService.getByName(teamDto.getName());
        } else return Response.status(Response.Status.BAD_REQUEST).build();

        if (teamDto.getName() != team.getName()) {
            teamService.updateName(team.getId(), teamDto.getName());
        }

        if (!teamDto.isActive()) {
            teamService.inactivateTeam(team.getId());
        } else {
            teamService.activateTeam(team.getId());
        }

        Collection<Long> userIds = teamDto.getUsersId();
        if (weHave(userIds)) {
            userIds.forEach(userId -> teamService.addUserToTeam(team.getId(), userId));
        }

        return Response.accepted().build();
    }
}
