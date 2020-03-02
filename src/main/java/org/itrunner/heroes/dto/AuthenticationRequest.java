package org.itrunner.heroes.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    @ApiModelProperty(value = "username", example = "admin", required = true)
    private String username;

    @ApiModelProperty(value = "password", example = "admin", required = true)
    private String password;
}