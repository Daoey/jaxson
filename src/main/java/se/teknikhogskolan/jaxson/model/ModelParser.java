package se.teknikhogskolan.jaxson.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.teknikhogskolan.springcasemanagement.model.Team;

public class ModelParser {

    public static TeamDto teamModelFrom(Team team) {
        List<Long> users = new ArrayList<>();
        team.getUsers().forEach(u -> {
            users.add(u.getId());
        });
        return new TeamDto(team.getName(), users, team.isActive());
    }

    public static Collection<TeamDto> teamModelsFromTeams(Iterable<Team> teams) {
        Collection<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(u -> teamDtos.add(teamModelFrom(u)));
        return teamDtos;
    }
}
