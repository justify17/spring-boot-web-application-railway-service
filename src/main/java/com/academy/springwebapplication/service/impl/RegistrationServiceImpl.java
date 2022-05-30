package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.UserDto;
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
    public void saveNewUser(UserDto userDto){
        User user = userMapper.userDtoToUser(userDto);

        String encryptedPassword = getEncryptedPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setRole(roleRepository.findByName("ROLE_USER"));
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public boolean isUserExists(UserDto userDto){
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if(existingUser != null){
            return true;
        }

        return false;
    }

    private String getEncryptedPassword(String password){
        return passwordEncoder.encode(password);
    }
}
