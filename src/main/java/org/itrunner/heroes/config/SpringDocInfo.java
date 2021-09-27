package org.itrunner.heroes.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "springdoc.info")
public class SpringDocInfo {
    private String title;
    private String description;
    private String termsOfService;
    private Contact contact;
    private String version;

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }
}
