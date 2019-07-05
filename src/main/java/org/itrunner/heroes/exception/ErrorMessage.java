package org.itrunner.heroes.exception;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Getter
@RequiredArgsConstructor
public class ErrorMessage {
    @NonNull
    private String type;

    @NonNull
    private String message;

    private List details = new ArrayList();

    public void addDetailMessage(String message) {
        this.details.add(message);
    }

    public void clearDetailMessage() {
        this.details.clear();
    }
}