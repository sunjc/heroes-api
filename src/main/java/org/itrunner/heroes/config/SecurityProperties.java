package org.itrunner.heroes.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private String[] ignorePaths;
    private String authPath;
    private Cors cors;
    private Jwt jwt;

    @Getter
    @Setter
    public static class Cors {
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
    }

    @Getter
    @Setter
    public static class Jwt {
        private String header;
        private String secret;
        private Long expiration;
        private String issuer;
    }
}