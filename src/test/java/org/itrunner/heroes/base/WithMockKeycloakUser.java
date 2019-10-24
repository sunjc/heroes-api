package org.itrunner.heroes.base;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockKeycloakUser {
    String username() default "admin";

    String email() default "admin@itrunner.org";

    String[] roles() default {"USER", "ADMIN"};
}