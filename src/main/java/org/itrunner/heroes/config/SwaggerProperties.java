package org.itrunner.heroes.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "springfox.documentation.swagger")
public class SwaggerProperties {
    private String title;
    private String description;
    private String version;
    private String basePackage;
    private String apiPath;
    private Contact contact;

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }
}
