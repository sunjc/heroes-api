package org.itrunner.heroes.base;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockKeycloakUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockKeycloakUser keycloakUser) {
        AccessToken accessToken = new AccessToken();
        accessToken.setPreferredUsername(keycloakUser.username());
        accessToken.setEmail(keycloakUser.email());
        accessToken.expiration(Integer.MAX_VALUE);
        accessToken.type("Bearer");

        RefreshableKeycloakSecurityContext keycloakSecurityContext = new RefreshableKeycloakSecurityContext(null, null, "access-token-string", accessToken, null, null, null);
        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = new KeycloakPrincipal<>("user-id", keycloakSecurityContext);

        HashSet<String> roles = new HashSet<>();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : keycloakUser.roles()) {
            roles.add(role);
            grantedAuthorities.add(new KeycloakRole(role));
        }
        KeycloakAccount account = new SimpleKeycloakAccount(principal, roles, keycloakSecurityContext);
        Authentication auth = new KeycloakAuthenticationToken(account, false, grantedAuthorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}