package com.academy.springwebapplication.annotation;

import com.academy.springwebapplication.validator.DepartureDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartureDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DepartureDateConstraint {
    String message() default "Invalid departure date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
