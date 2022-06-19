package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChangePasswordValidator implements Validator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangedPasswordDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangedPasswordDto changedPasswordDto = (ChangedPasswordDto) target;

        if (!changedPasswordDto.getPassword().trim().isEmpty() && isOldPasswordNotMatching(changedPasswordDto)) {
            errors.rejectValue("password", "error.password", "Invalid old password");
        }

        if (!changedPasswordDto.getConfirmNewPassword().trim().isEmpty() && isNewPasswordsNotEqual(changedPasswordDto)) {
            errors.rejectValue("confirmNewPassword", "error.confirmNewPassword",
                    "New passwords do not match");
        }
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
