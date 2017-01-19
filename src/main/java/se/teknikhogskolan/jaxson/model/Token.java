package se.teknikhogskolan.jaxson.model;

public final class Token {

    private String token;
    private long expirationTime;

    public Token(String token, long expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    protected Token(){}

    public String getToken() {
        return token;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
