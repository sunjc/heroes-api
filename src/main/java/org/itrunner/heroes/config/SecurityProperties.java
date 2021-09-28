package org.itrunner.heroes.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private String[] ignorePaths;
    private Cors cors;
    private Jwt jwt;

    @Getter
    @Setter
    public static class Cors {
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
    }

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private Long expiration;
        private String issuer;
    }
}