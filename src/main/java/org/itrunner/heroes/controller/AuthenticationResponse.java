package org.itrunner.heroes.controller;

import java.util.Set;

public class AuthenticationResponse {
    private String token;
    private Set<String> authorities;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, Set<String> authorities) {
        this.token = token;
        this.authorities = authorities;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
