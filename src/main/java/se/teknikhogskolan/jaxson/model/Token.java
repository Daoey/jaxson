package se.teknikhogskolan.jaxson.model;

public final class Token {

    private final String token;
    private final String expirationDate;

    public Token(String token, String expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
