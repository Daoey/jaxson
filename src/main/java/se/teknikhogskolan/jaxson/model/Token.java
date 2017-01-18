package se.teknikhogskolan.jaxson.model;

public final class Token {

    private final String token;
    private final String expirationTime;

    public Token(String token, String expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public String getExpirationTime() {
        return expirationTime;
    }
}
