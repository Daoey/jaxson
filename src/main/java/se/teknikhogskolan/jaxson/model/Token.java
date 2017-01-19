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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Token{");
        sb.append("token='").append(token).append('\'');
        sb.append(", expirationTime=").append(expirationTime);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Token token1 = (Token) object;

        if (expirationTime != token1.expirationTime) return false;
        return token != null ? token.equals(token1.token) : token1.token == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (int) (expirationTime ^ (expirationTime >>> 32));
        return result;
    }
}
