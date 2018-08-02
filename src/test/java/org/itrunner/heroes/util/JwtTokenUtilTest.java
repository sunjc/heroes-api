package org.itrunner.heroes.util;

import org.itrunner.heroes.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenUtilTest {
    @Mock
    private Config config;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setup() {
        Config.Jwt jwtConfig = new Config.Jwt();
        jwtConfig.setIssuer("ITRunner");
        jwtConfig.setSecret("mySecret");
        jwtConfig.setExpiration(7200L);

        given(config.getJwt()).willReturn(jwtConfig);
    }

    @Test
    public void generate_token() {
        String token = jwtTokenUtil.generate("Jason");
        assertThat(jwtTokenUtil.verify(token)).isEqualTo("Jason");
    }
}
