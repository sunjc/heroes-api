package org.itrunner.heroes.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorMessage {
    private String type;
    private String message;
    private List<String> details = new ArrayList<>();

    public ErrorMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public void addDetailMessage(String message) {
        this.details.add(message);
    }
}