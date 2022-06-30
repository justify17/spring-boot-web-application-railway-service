package com.academy.springwebapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity does not exist")
public class EntityByIdNotFoundException extends RuntimeException {
    public EntityByIdNotFoundException(Integer id) {
        super(String.format("Entity with id %d does not exist", id));
    }
}
