package org.itrunner.heroes.config;

import org.itrunner.heroes.util.KeycloakContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return KeycloakContext.getUsername();
    }
}
