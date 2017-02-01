package se.teknikhogskolan.jaxson.security;

import java.time.Instant;
import se.teknikhogskolan.jaxson.model.Token;
import se.teknikhogskolan.springcasemanagement.security.JwtBuilder;

import static se.teknikhogskolan.jaxson.security.TokenLifeLengths.ACCESS;
import static se.teknikhogskolan.jaxson.security.TokenLifeLengths.REFRESH;

public class JwtHelper {

    public Token generateAccessToken(String username) {

        JwtBuilder jwtBuilder = new JwtBuilder();
        jwtBuilder.putClaim("sub", "access");
        Long exp = getAccessExpiration();
        jwtBuilder.putClaim("exp", exp.toString());
        jwtBuilder.putClaim("username", username);

        return new Token(jwtBuilder.build(), exp.longValue());
    }

    private Long getAccessExpiration() {
        return Instant.now().getEpochSecond() + ACCESS;
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
        return Instant.now().getEpochSecond() + REFRESH;
    }

}
