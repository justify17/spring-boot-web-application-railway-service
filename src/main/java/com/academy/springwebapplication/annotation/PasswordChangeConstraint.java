package com.academy.springwebapplication.annotation;

import com.academy.springwebapplication.validator.ChangePasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChangePasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordChangeConstraint {
    String message() default "Unsuccessful password change";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
