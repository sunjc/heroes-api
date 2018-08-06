package org.itrunner.heroes.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.itrunner.heroes.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final Log LOG = LogFactory.getLog(JwtTokenUtil.class);

    private static final String CLAIM_AUTHORITIES = "authorities";

    @Autowired
    private Config config;

    public String generate(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            return JWT.create()
                    .withIssuer(config.getJwt().getIssuer())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + config.getJwt().getExpiration() * 1000))
                    .withSubject(user.getUsername())
                    .withArrayClaim(CLAIM_AUTHORITIES, AuthorityUtil.getAuthorities(user))
                    .sign(algorithm);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @param token
     * @return username
     */
    public UserDetails verify(String token) {
        if (token == null) {
            return null;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(config.getJwt().getIssuer()).build();
            DecodedJWT jwt = verifier.verify(token);
            return new User(jwt.getSubject(), "N/A", AuthorityUtil.createGrantedAuthorities(jwt.getClaim(CLAIM_AUTHORITIES).asArray(String.class)));
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }
    }
}