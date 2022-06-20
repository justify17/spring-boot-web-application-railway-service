package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.PasswordChangeConstraint;
import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ChangePasswordValidator implements ConstraintValidator<PasswordChangeConstraint, ChangedPasswordDto> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(ChangedPasswordDto changedPasswordDto, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;

        if (!changedPasswordDto.getPassword().trim().isEmpty() && isOldPasswordNotMatching(changedPasswordDto)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid old password")
                    .addPropertyNode("password")
                    .addConstraintViolation();

            valid = false;
        }

        if (!changedPasswordDto.getConfirmNewPassword().trim().isEmpty() && isNewPasswordsNotEqual(changedPasswordDto)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("New passwords do not match")
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();

            valid = false;
        }

        if(!valid){
            constraintValidatorContext.disableDefaultConstraintViolation();
        }

        return valid;
    }

    private boolean isOldPasswordNotMatching(ChangedPasswordDto changedPasswordDto) {
        String oldPassword = changedPasswordDto.getPassword();

        User user = userRepository.findByUsername(changedPasswordDto.getUsername());

        return !passwordEncoder.matches(oldPassword, user.getPassword());
    }

    private boolean isNewPasswordsNotEqual(ChangedPasswordDto changedPasswordDto) {
        String newPassword = changedPasswordDto.getNewPassword();
        String confirmNewPassword = changedPasswordDto.getConfirmNewPassword();

        return !newPassword.equals(confirmNewPassword);
    }
}
