package org.itrunner.heroes.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(code = NOT_FOUND)
public class HeroNotFoundException extends RuntimeException {
    public HeroNotFoundException(Long id) {
        this("Could not find hero with id '%s'", id);
    }

    public HeroNotFoundException(String name) {
        this("Could not find hero with name '%s'", name);
    }

    public HeroNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}