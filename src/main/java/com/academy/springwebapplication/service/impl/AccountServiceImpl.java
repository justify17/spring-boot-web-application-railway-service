package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.UserInformationDto;
import com.academy.springwebapplication.mapper.UserInformationMapper;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.entity.UserInformation;
import com.academy.springwebapplication.model.repository.UserInformationRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import com.academy.springwebapplication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final UserInformationMapper userInformationMapper;

    @Override
    public UserInformationDto getUserInformation(String username) {
        User user = userRepository.findByUsername(username);

        UserInformation userInformation = user.getUserInformation();

        if (userInformation == null) {
            return new UserInformationDto();
        }

        return userInformationMapper.userInformationToUserInformationDto(userInformation);
    }

    @Override
    public void saveUserInformation(UserInformationDto userInformationDto, String username) {
        User user = userRepository.findByUsername(username);

        UserInformation userInformation = userInformationMapper.userInformationDtoToUserInformation(userInformationDto);

        if (user.getUserInformation() != null) {
            userInformation.setId(user.getId());
        }

        userInformation.setUser(user);

        userInformationRepository.save(userInformation);
    }
}
