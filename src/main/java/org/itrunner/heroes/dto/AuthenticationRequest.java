package org.itrunner.heroes.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthenticationRequest {
    @ApiModelProperty(value = "username", example = "admin", required = true)
    @NotNull
    private String username;

    @ApiModelProperty(value = "password", example = "admin", required = true)
    @NotNull
    private String password;
}