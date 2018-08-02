package org.itrunner.heroes.controller;

import io.swagger.annotations.ApiModelProperty;

public class AuthenticationRequest {
    @ApiModelProperty(value = "username", example = "admin", required = true)
    private String username;

    @ApiModelProperty(value = "password", example = "admin", required = true)
    private String password;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
