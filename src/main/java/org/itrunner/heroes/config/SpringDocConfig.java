package org.itrunner.heroes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    private static final String SCHEME = "bearer";
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    private static final String BEARER_FORMAT = "JWT";

    private final SpringDocInfo info;

    @Autowired
    public SpringDocConfig(SpringDocInfo info) {
        this.info = info;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addSecurityItem(securityRequirement()).components(securityComponents()).info(info());
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
    }

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(SCHEME).bearerFormat(BEARER_FORMAT));
    }

    private Info info() {
        Contact contact = new Contact()
                .name(info.getContact().getName())
                .url(info.getContact().getUrl())
                .email(info.getContact().getEmail());

        return new Info()
                .title(info.getTitle())
                .description(info.getDescription())
                .termsOfService(info.getTermsOfService())
                .contact(contact)
                .version(info.getVersion());
    }
}