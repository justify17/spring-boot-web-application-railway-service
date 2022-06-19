package com.academy.springwebapplication.annotation;

import com.academy.springwebapplication.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameNotTaken {
    String message() default "User with this username already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
