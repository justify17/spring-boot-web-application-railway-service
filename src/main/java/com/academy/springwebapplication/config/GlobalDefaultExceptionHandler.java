package com.academy.springwebapplication.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_TEMPLATE = "error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
    public String defaultErrorHandler() {

        return DEFAULT_ERROR_TEMPLATE;
    }
}
