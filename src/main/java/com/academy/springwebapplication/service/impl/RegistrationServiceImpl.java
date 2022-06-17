package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.UserRegistrationDto;
import com.academy.springwebapplication.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public void saveNewUser(UserRegistrationDto userRegistrationDto){
        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setRole(roleRepository.findByName("ROLE_USER"));
        user.setAccountNonLocked(true);

        userRepository.save(user);
    }

    @Override
    public boolean isUserExisting(String username){
        User existingUser = userRepository.findByUsername(username);

        if(existingUser != null){
            return true;
        }

        return false;
    }
}
