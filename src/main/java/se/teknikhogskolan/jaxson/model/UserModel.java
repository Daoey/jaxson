package se.teknikhogskolan.jaxson.model;

import java.util.Collection;

public final class UserModel extends AbstractEntity{

    private Long id;
    private Long userNumber;
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;
    private Long teamId;
    private Collection<WorkItem> workItems;

    protected UserModel() {
    }

    public UserModel(Long id, Long userNumber, String username, String firstName, String lastName) {
        this.id = id;
        this.userNumber = userNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamId = null;
        this.active = true;
    }

    @Override
    public Long getId() {
        return id;
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

    public Long getTeamId() {
        return teamId;
    }

    public UserModel setTeamId(Long teamModel) {
        this.teamId = teamModel;
        return this;
    }

    public Collection<WorkItem> getWorkItems() {
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
        //builder.append(teamId == null ? "null" : teamId.getId());
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
}
