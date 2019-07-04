package org.itrunner.heroes.exception;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel
@Getter
@AllArgsConstructor
public class ErrorMessage {
    private String type;
    private String message;
}