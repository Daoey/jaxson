package se.teknikhogskolan.jaxson.security;

import java.time.Instant;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.springcasemanagement.security.JwtBuilder;

public class JwtHelper {

    private final int loginDuration = 60; // TODO make login last longer
    private final int refreshDuration = 60 * 60 * 24 * 7;

    public Token generateAuthorizationToken(String username) {

        JwtBuilder jwtBuilder = new JwtBuilder();
        jwtBuilder.putClaim("sub", "authorization");
        Long exp = getLoginExpiration();
        jwtBuilder.putClaim("exp", exp.toString());
        jwtBuilder.putClaim("username", username);

        return new Token(jwtBuilder.build(), exp.longValue());
    }

    private Long getLoginExpiration() {
        return Instant.now().getEpochSecond() + loginDuration;
    }

    public Token generateRefreshToken(String username) {

        JwtBuilder jwtBuilder = new JwtBuilder();
        jwtBuilder.putClaim("sub", "refresh");
        Long exp = getRefreshExpiration();
        jwtBuilder.putClaim("exp", exp.toString());
        jwtBuilder.putClaim("username", username);

        return new Token(jwtBuilder.build(), exp.longValue());
    }

    private Long getRefreshExpiration() {
        return Instant.now().getEpochSecond() + refreshDuration;
    }

}
