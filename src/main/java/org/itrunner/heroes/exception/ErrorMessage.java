package org.itrunner.heroes.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Date;

@ApiModel
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorMessage {
    private Date timestamp;
    private String error;
    private String message;

    public ErrorMessage() {
        this.timestamp = new Date();
    }

    public ErrorMessage(String error, String message) {
        this();
        this.error = error;
        this.message = message;
    }
}