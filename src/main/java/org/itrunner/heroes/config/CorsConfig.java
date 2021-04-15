package org.itrunner.heroes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private final SecurityProperties securityProperties;

    public CorsConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        SecurityProperties.Cors cors = securityProperties.getCors();

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(cors.getAllowedMethods())
                        .allowedHeaders(cors.getAllowedHeaders())
                        .allowedOrigins(cors.getAllowedOrigins());
            }
        };
    }

}
