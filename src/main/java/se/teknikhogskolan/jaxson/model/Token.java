package se.teknikhogskolan.jaxson.model;

public final class Token {

    private final String token;
    private final long expirationTime;

    public Token(String token, long expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
