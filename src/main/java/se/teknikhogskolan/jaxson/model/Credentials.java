package se.teknikhogskolan.jaxson.model;

public final class Credentials {

    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected Credentials() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
