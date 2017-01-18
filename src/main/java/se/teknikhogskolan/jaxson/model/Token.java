package se.teknikhogskolan.jaxson.model;

public final class Token {

    private final String token;
    private final int expirationTime;

    public Token(String token, int expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public int getExpirationTime() {
        return expirationTime;
    }
}
