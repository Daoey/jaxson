package se.teknikhogskolan.jaxson.model;

import se.teknikhogskolan.springcasemanagement.model.User;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;

import java.util.ArrayList;
import java.util.Collection;

public final class UserModel extends AbstractEntity{

    private Long userNumber;
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;
    private TeamModel team;
    private Collection<WorkItemModel> workItems;

    protected UserModel() {
    }

    public UserModel(User user) {
        setId(user.getId());
        setCreated(user.getCreated());
        setLastModified(user.getLastModified());
        this.userNumber = user.getUserNumber();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.active = user.isActive();
        //this.team = new TeamModel(user.getTeam());
        this.workItems = addWorkItems(user);
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public UserModel setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TeamModel getTeam() {
        return team;
    }

    public void setTeam(TeamModel team) {
        this.team = team;
    }

    public void setWorkItems(Collection<WorkItemModel> workItems) {
        this.workItems = workItems;
    }

    public Collection<WorkItemModel> getWorkItems() {
        return workItems;
    }

    public boolean isActive() {
        return active;
    }

    public UserModel setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserModel) {
            UserModel otherUser = (UserModel) obj;
            return userNumber.equals(otherUser.getUserNumber()) && username.equals(otherUser.getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result += result * userNumber.hashCode();
        result += result * username.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserModel [id=");
        builder.append(getId());
        builder.append(", userNumber=");
        builder.append(userNumber);
        builder.append(", username=");
        builder.append(username);
        builder.append(", firstName=");
        builder.append(firstName == null ? "null" : firstName);
        builder.append(", lastName=");
        builder.append(lastName == null ? "null" : lastName);
        builder.append(", teamId=");
        builder.append(team == null ? "null" : team.getId());
        builder.append(", workItemsSize=");
        builder.append(workItems == null ? "0" : workItems.size());
        builder.append(", active=");
        builder.append(active);
        builder.append(", created=");
        builder.append(createdDateToString());
        builder.append(", lastModified=");
        builder.append(lastModifiedToString());
        builder.append("]");
        return builder.toString();
    }

    private Collection<WorkItemModel> addWorkItems(User user){
        Collection<WorkItemModel> workItemModels = new ArrayList<>();
        if(user.getWorkItems() != null) {
            for (WorkItem workItem : user.getWorkItems()) {
                workItemModels.add(new WorkItemModel(workItem));
            }
        }
        return workItemModels;
    }
}
