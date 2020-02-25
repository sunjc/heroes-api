package org.itrunner.heroes.util;

import org.itrunner.heroes.config.SecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class JwtTokenUtilTest {
    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        SecurityProperties.Jwt jwtConfig = new SecurityProperties.Jwt();
        jwtConfig.setIssuer("ITRunner");
        jwtConfig.setSecret("mySecret");
        jwtConfig.setExpiration(7200L);

        given(securityProperties.getJwt()).willReturn(jwtConfig);
    }

    @Test
    public void generate_token() {
        UserDetails user = new User("Jason", "N/A", AuthorityUtil.createGrantedAuthorities("ADMIN"));
        String token = jwtTokenUtil.generate(user);
        assertThat(jwtTokenUtil.verify(token).getUsername()).isEqualTo("Jason");
    }
}
