package org.itrunner.heroes.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.itrunner.heroes.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final Log LOG = LogFactory.getLog(JwtTokenUtil.class);

    @Autowired
    private Config config;

    public String generate(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            return JWT.create()
                    .withIssuer(config.getJwt().getIssuer())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + config.getJwt().getExpiration() * 1000))
                    .withSubject(username)
                    .sign(algorithm);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @param token
     * @return username
     */
    public String verify(String token) {
        if (token == null) {
            return null;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(config.getJwt().getIssuer()).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }
    }
}