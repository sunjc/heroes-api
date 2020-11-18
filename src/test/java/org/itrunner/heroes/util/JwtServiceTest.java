package org.itrunner.heroes.util;

import org.itrunner.heroes.config.SecurityProperties;
import org.itrunner.heroes.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class JwtServiceTest {
    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private JwtService jwtService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        SecurityProperties.Jwt jwtConfig = new SecurityProperties.Jwt();
        jwtConfig.setIssuer("ITRunner");
        jwtConfig.setSecret("mySecret");
        jwtConfig.setExpiration(7200L);

        given(securityProperties.getJwt()).willReturn(jwtConfig);
    }

    @AfterEach
    public void release() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void generate_token() {
        UserDetails user = new User("Jason", "N/A", AuthorityUtils.toGrantedAuthorities("ADMIN"));
        String token = jwtService.generate(user);
        assertThat(jwtService.verify(token).getUsername()).isEqualTo("Jason");
    }
}
