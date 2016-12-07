package se.teknikhogskolan.jaxson.model;

import se.teknikhogskolan.springcasemanagement.model.Team;
import se.teknikhogskolan.springcasemanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class ModelParser {

    public static TeamModel teamModelFrom(Team team) {
        List<Long> users = new ArrayList<>();
        team.getUsers().forEach(u -> {
            users.add(u.getId());
        });
        return new TeamModel(team.getId(), team.getName(), users, team.isActive());
    }

    //TODO delete
/*    public static List<UserModel> userModelsFromUsers(List<User> users) {
        List<UserModel> userModels = new ArrayList<>();
        users.forEach(u -> userModels.add(userModelFrom(u)));
        return userModels;
    }

    public static UserModel userModelFrom(User user) {
        UserModel userModel = new UserModel(user.getId(),
                                            user.getUserNumber(),
                                            user.getUsername(),
                                            user.getFirstName(),
                                            user.getLastName());
        userModel.setTeamId(null == user.getTeam() ? null : user.getTeam().getId());
        userModel.setActive(user.isActive());
        return userModel;
    }*/
}
