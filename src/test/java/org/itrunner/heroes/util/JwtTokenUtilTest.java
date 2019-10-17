package org.itrunner.heroes.util;

import org.itrunner.heroes.config.SecurityProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenUtilTest {
    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setup() {
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
