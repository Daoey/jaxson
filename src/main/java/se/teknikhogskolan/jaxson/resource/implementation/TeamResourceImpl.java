package se.teknikhogskolan.jaxson.resource.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import se.teknikhogskolan.jaxson.model.TeamDto;
import se.teknikhogskolan.jaxson.model.UserDto;
import se.teknikhogskolan.jaxson.resource.TeamResource;
import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.service.TeamService;
import se.teknikhogskolan.springcasemanagement.service.UserService;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelFrom;
import static se.teknikhogskolan.jaxson.model.ModelParser.teamModelsFromTeams;

public class TeamResourceImpl implements TeamResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Override
    public Response addUserToTeam(Long teamId, Long userId) {
        teamService.addUserToTeam(teamId, userId);
        return Response.accepted().build();
    }

    @Override
    public Response createTeam(String name) {
        TeamDto teamDto = teamModelFrom(teamService.create(name));
        return Response.status(Response.Status.CREATED).header("Location", "teams?id=" + teamDto.getId()).build();
    }

    @Override
    public Response getTeam(Long id) {
        if (weHaveA(id)) {
            Team team = teamService.getById(id);
            TeamDto teamDto = teamModelFrom(team);
            return Response.ok(teamDto).build();
        }
        Collection<TeamDto> teamDtos = teamModelsFromTeams(teamService.getAll());
        return Response.ok(teamDtos).build();
    }

    private boolean weHaveA(Object o) {
        return null != o;
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
        return null;
    }
}
