package org.itrunner.heroes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@SuppressWarnings("SpringJavaAutowiringInspection")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ROLE_ADMIN = "ADMIN";

    @Value("${api.base-path}/**")
    private String apiPath;

    @Value("${management.endpoints.web.exposure.include}")
    private String[] actuatorExposures;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final SecurityProperties securityProperties;

    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, SecurityProperties securityProperties, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.securityProperties = securityProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getIgnorePaths());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // don't create session
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to(actuatorExposures)).permitAll()
                .antMatchers(OPTIONS, "/**").permitAll()
                .antMatchers(POST, apiPath).hasRole(ROLE_ADMIN)
                .antMatchers(PUT, apiPath).hasRole(ROLE_ADMIN)
                .antMatchers(DELETE, apiPath).hasRole(ROLE_ADMIN)
                .anyRequest().authenticated().and()
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class) // Custom JWT based security filter
                .headers().cacheControl(); // disable page caching
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() {
        return new AuthenticationTokenFilter();
    }
}