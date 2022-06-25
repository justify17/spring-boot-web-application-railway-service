package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.UserRegistrationDto;
import com.academy.springwebapplication.service.RegistrationService;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestObjectFactory testObjectFactory;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void whenRegistration() throws Exception {
        this.mockMvc.perform(get("/registration"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    void whenUserRegistration_AndModelIsValid() throws Exception {
        UserRegistrationDto user = testObjectFactory.getValidUserRegistrationDto();

        this.mockMvc.perform(post("/registration")
                        .param("username", user.getUsername()).param("password", user.getPassword()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        verify(registrationService, times(1)).saveNewUser(user);
        verifyNoMoreInteractions(registrationService);
    }

    @Test
    void whenUserRegistration_AndModelIsInvalid() throws Exception {
        UserRegistrationDto user = testObjectFactory.getInvalidUserRegistrationDto();

        this.mockMvc.perform(post("/registration")
                        .param("username", user.getUsername()).param("password", user.getPassword()))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "username", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));

        verify(registrationService, never()).saveNewUser(user);
    }
}
