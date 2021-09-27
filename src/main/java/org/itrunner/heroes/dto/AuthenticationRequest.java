package org.itrunner.heroes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthenticationRequest {
    @Schema(name = "username", example = "admin", required = true)
    @NotNull
    private String username;

    @Schema(name = "password", example = "admin", required = true)
    @NotNull
    private String password;
}