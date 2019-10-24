package org.itrunner.heroes.util;

import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class KeycloakContext {
    private KeycloakContext() {
    }

    public static Optional<AccessToken> getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getCredentials() instanceof RefreshableKeycloakSecurityContext)) {
            return empty();
        }
        return of(((RefreshableKeycloakSecurityContext) authentication.getCredentials()).getToken());
    }

    public static Optional<String> getUsername() {
        Optional<AccessToken> accessToken = getAccessToken();
        return accessToken.map(AccessToken::getPreferredUsername);
    }

    public static Optional<String> getEmail() {
        Optional<AccessToken> accessToken = getAccessToken();
        return accessToken.map(AccessToken::getEmail);
    }

}
