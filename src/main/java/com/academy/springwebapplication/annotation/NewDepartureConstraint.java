package com.academy.springwebapplication.annotation;

import com.academy.springwebapplication.validator.NewDepartureValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NewDepartureValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewDepartureConstraint {
    String message() default "Failed to create a new departure";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
