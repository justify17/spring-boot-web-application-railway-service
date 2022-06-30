package com.academy.springwebapplication.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void accessTest_whenMainPage_AndUserIsAnonymous() throws Exception {

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void accessTest_whenRegistrationPage_AndUserIsAnonymous() throws Exception {

        this.mockMvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void accessTest_whenLoginPage_AndUserIsAnonymous() throws Exception {

        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void accessTest_whenDeparturesPage_AndUserIsAnonymous() throws Exception {

        this.mockMvc.perform(get("/departures"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser
    void accessTest_whenDeparturesPage_AndUserIsNotAnonymous() throws Exception {

        this.mockMvc.perform(get("/departures"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void accessTest_whenAdminPage_AndUserIsNotAdmin() throws Exception {

        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void accessTest_whenAdminDepartureDetailsPage_AndUserIsNotAdmin() throws Exception {

        this.mockMvc.perform(get("/admin/departureDetails")
                        .param("departureId", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void accessTest_whenAdminPage_AndUserIsAdmin() throws Exception {

        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void successfulLoginTest() throws Exception {

        this.mockMvc.perform(formLogin().user("user").password("user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void badCredentialsLoginTest() throws Exception {

        this.mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "invalidPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}
