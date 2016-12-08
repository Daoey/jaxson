package se.teknikhogskolan.jaxson.model;

import se.teknikhogskolan.springcasemanagement.model.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModelParser {

    public static TeamModel teamModelFrom(Team team) {
        List<Long> users = new ArrayList<>();
        team.getUsers().forEach(u -> {
            users.add(u.getId());
        });
        return new TeamModel(team.getId(), team.getName(), users, team.isActive());
    }

    public static Collection<TeamModel> teamModelsFromTeams(Iterable<Team> teams) {
        Collection<TeamModel> teamModels = new ArrayList<>();
        teams.forEach(u -> teamModels.add(teamModelFrom(u)));
        return teamModels;
    }
}
