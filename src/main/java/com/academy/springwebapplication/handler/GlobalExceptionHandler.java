package com.academy.springwebapplication.handler;

import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.exception.EntityByTitleNotFoundException;
import com.academy.springwebapplication.exception.FailedPaymentException;
import com.academy.springwebapplication.exception.FailedSavingTicketException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_TEMPLATE = "error";

    @ExceptionHandler({EntityByIdNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
    public String handleEntityByIdNotFoundException() {

        return DEFAULT_ERROR_TEMPLATE;
    }

    @ExceptionHandler({EntityByTitleNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
    public String handleEntityByTitleNotFoundException() {

        return DEFAULT_ERROR_TEMPLATE;
    }

    @ExceptionHandler({FailedPaymentException.class})
    public String handleFailedPaymentException(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());

        return DEFAULT_ERROR_TEMPLATE;
    }

    @ExceptionHandler({FailedSavingTicketException.class})
    public String handleFailedSavingTicketException(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());

        return DEFAULT_ERROR_TEMPLATE;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
    public String defaultErrorHandler() {

        return DEFAULT_ERROR_TEMPLATE;
    }
}
