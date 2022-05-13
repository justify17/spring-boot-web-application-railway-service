package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.RoleRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import com.academy.springwebapplication.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveNewUser(User user){
        String encryptedPassword = getEncryptedPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setRole(roleRepository.findByName("ROLE_USER"));
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public boolean isUserExists(User user){
        User existingUser = userRepository.findByUsername(user.getUsername());

        if(existingUser != null){
            return true;
        }

        return false;
    }

    private String getEncryptedPassword(String password){
        return passwordEncoder.encode(password);
    }
}
