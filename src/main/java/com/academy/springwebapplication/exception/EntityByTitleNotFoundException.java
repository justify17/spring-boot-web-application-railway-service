package com.academy.springwebapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity does not exist")
public class EntityByTitleNotFoundException extends RuntimeException {
    public EntityByTitleNotFoundException(String title) {
        super(String.format("Entity with title %s does not exist", title));
    }
}
