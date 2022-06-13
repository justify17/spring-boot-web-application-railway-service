package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.ChangedAccountDataDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.mapper.UserInformationMapper;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.entity.UserInformation;
import com.academy.springwebapplication.model.repository.UserInformationRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import com.academy.springwebapplication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final UserInformationMapper userInformationMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ChangedUserInformationDto getUserInformation(String username) {
        User user = userRepository.findByUsername(username);

        UserInformation userInformation = user.getUserInformation();

        if (userInformation == null) {
            ChangedUserInformationDto changedUserInformationDto = new ChangedUserInformationDto();
            changedUserInformationDto.setUsername(username);

            return changedUserInformationDto;
        }

        return userInformationMapper.userInformationToUserInformationDto(userInformation);
    }

    @Override
    public void saveUserInformation(ChangedUserInformationDto changedUserInformationDto) {
        User user = userRepository.findByUsername(changedUserInformationDto.getUsername());

        UserInformation userInformation = userInformationMapper.userInformationDtoToUserInformation(changedUserInformationDto);

        if (user.getUserInformation() != null) {
            userInformation.setId(user.getId());
        }

        userInformation.setUser(user);

        userInformationRepository.save(userInformation);
    }

    @Override
    public void saveNewUsername(ChangedAccountDataDto changedAccountDataDto) {
        User user = userRepository.findByUsername(changedAccountDataDto.getUsername());

        user.setUsername(changedAccountDataDto.getNewUsername());

        userRepository.save(user);
    }

    @Override
    public void saveNewPassword(ChangedAccountDataDto changedAccountDataDto) {
        User user = userRepository.findByUsername(changedAccountDataDto.getUsername());

        String encryptedPassword = passwordEncoder.encode(changedAccountDataDto.getNewPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }
}
