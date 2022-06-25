package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.dto.ChangedUsernameDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.service.AccountService;
import com.academy.springwebapplication.service.TicketService;
import com.academy.springwebapplication.validator.ChangePasswordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = AccountControllerTest.MOCK_USERNAME, password = AccountControllerTest.MOCK_PASSWORD)
class AccountControllerTest {

    public static final String MOCK_USERNAME = "user";
    public static final String MOCK_PASSWORD = "user";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private ChangePasswordValidator changePasswordValidator;

    @Test
    void whenAccount() throws Exception {
        when(accountService.getUserInformation(MOCK_USERNAME)).thenReturn(new ChangedUserInformationDto());

        this.mockMvc.perform(get("/account"))
                .andExpect(model().attributeExists("openDefault"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, times(1)).getUserInformation(MOCK_USERNAME);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    void whenCancelOrder_AndRequestParamIsValid() throws Exception {
        Integer ticketId = 1;

        doNothing().when(ticketService).checkIfTicketIdIsValid(ticketId);

        when(accountService.getUserInformation(MOCK_USERNAME)).thenReturn(new ChangedUserInformationDto());

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "cancelOrder")
                        .param("ticketId", String.valueOf(ticketId)))
                .andExpect(model().attributeExists("openDefault"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(ticketService, times(1)).checkIfTicketIdIsValid(ticketId);
        verify(ticketService, times(1)).deleteTicketById(ticketId);
        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, times(1)).getUserInformation(MOCK_USERNAME);

        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    void whenCancelOrder_AndRequestParamIsInvalid() throws Exception {
        Integer ticketId = 0;

        doThrow(new EntityByIdNotFoundException(ticketId))
                .when(ticketService)
                .checkIfTicketIdIsValid(ticketId);

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "cancelOrder")
                        .param("ticketId", String.valueOf(ticketId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityByIdNotFoundException));

        verify(ticketService, times(1)).checkIfTicketIdIsValid(ticketId);
        verify(ticketService, never()).deleteTicketById(ticketId);
        verify(ticketService, never()).getUserTickets(MOCK_USERNAME);
        verify(accountService, never()).getUserInformation(MOCK_USERNAME);
    }

    @Test
    void whenChangeUserInformation_AndModelIsValid() throws Exception {
        ChangedUserInformationDto userInformation = new ChangedUserInformationDto();
        userInformation.setUsername(MOCK_USERNAME);
        userInformation.setFirstName("TestName");
        userInformation.setSurname("TestSurname");
        userInformation.setPhoneNumber("+375(25)832-95-63");

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changeInformation")
                        .param("username", userInformation.getUsername())
                        .param("firstName", userInformation.getFirstName())
                        .param("surname", userInformation.getSurname())
                        .param("phoneNumber", userInformation.getPhoneNumber()))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("openInformation"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(model().attributeExists("successfulSave"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(accountService, times(1)).saveUserInformation(userInformation);
        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, never()).getUserInformation(MOCK_USERNAME);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    void whenChangeUserInformation_AndModelIsValid_AndSetToEmptyUserInformation() throws Exception {
        ChangedUserInformationDto userInformation = new ChangedUserInformationDto();
        userInformation.setUsername(MOCK_USERNAME);
        userInformation.setFirstName("");
        userInformation.setSurname("");
        userInformation.setPhoneNumber("");

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changeInformation")
                        .param("username", userInformation.getUsername())
                        .param("firstName", userInformation.getFirstName())
                        .param("surname", userInformation.getSurname())
                        .param("phoneNumber", userInformation.getPhoneNumber()))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("openInformation"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(model().attributeExists("successfulSave"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(accountService, times(1)).saveUserInformation(userInformation);
        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, never()).getUserInformation(MOCK_USERNAME);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    void whenChangeUserInformation_AndModelIsInvalid() throws Exception {
        ChangedUserInformationDto userInformation = new ChangedUserInformationDto();
        userInformation.setUsername(MOCK_USERNAME);
        userInformation.setFirstName("Test+Invalid    Name @@ 123");
        userInformation.setSurname("Test_Invalid_Surname");
        userInformation.setPhoneNumber("232");

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changeInformation")
                        .param("username", userInformation.getUsername())
                        .param("firstName", userInformation.getFirstName())
                        .param("surname", userInformation.getSurname())
                        .param("phoneNumber", userInformation.getPhoneNumber()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userInformation",
                        "firstName", "surname", "phoneNumber"))
                .andExpect(model().attributeExists("openInformation"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(model().attributeDoesNotExist("successfulSave"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(accountService, never()).saveUserInformation(userInformation);
        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, never()).getUserInformation(MOCK_USERNAME);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    void whenChangeUsername_AndModelIsValid() throws Exception {
        ChangedUsernameDto changedUsernameDto = new ChangedUsernameDto();
        changedUsernameDto.setUsername(MOCK_USERNAME);
        changedUsernameDto.setNewUsername("newValidUsername");

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changeUsername")
                        .param("username", changedUsernameDto.getUsername())
                        .param("newUsername", changedUsernameDto.getNewUsername()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        verify(accountService, times(1)).saveNewUsername(changedUsernameDto);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void whenChangeUsername_AndModelIsInvalid() throws Exception {
        ChangedUsernameDto changedUsernameDto = new ChangedUsernameDto();
        changedUsernameDto.setUsername(MOCK_USERNAME);
        changedUsernameDto.setNewUsername("Неправильное     New_Username");

        when(accountService.getUserInformation(MOCK_USERNAME)).thenReturn(new ChangedUserInformationDto());

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changeUsername")
                        .param("username", changedUsernameDto.getUsername())
                        .param("newUsername", changedUsernameDto.getNewUsername()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("changedUsername", "newUsername"))
                .andExpect(model().attributeExists("openChangeUsername"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(accountService, never()).saveNewUsername(changedUsernameDto);
        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, times(1)).getUserInformation(MOCK_USERNAME);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    void whenChangePassword_AndModelIsValid() throws Exception {
        ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
        changedPasswordDto.setUsername(MOCK_USERNAME);
        changedPasswordDto.setPassword(MOCK_PASSWORD);
        changedPasswordDto.setNewPassword("newPassword");
        changedPasswordDto.setConfirmNewPassword("newPassword");

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changePassword")
                        .param("username", changedPasswordDto.getUsername())
                        .param("password", changedPasswordDto.getPassword())
                        .param("newPassword", changedPasswordDto.getNewPassword())
                        .param("confirmNewPassword", changedPasswordDto.getConfirmNewPassword()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        verify(accountService, times(1)).saveNewPassword(changedPasswordDto);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void whenChangePassword_AndModelIsInvalid() throws Exception {
        ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
        changedPasswordDto.setUsername(MOCK_USERNAME);
        changedPasswordDto.setPassword("InvalidOldPassword");
        changedPasswordDto.setNewPassword("12345");
        changedPasswordDto.setConfirmNewPassword("123");

        when(accountService.getUserInformation(MOCK_USERNAME)).thenReturn(new ChangedUserInformationDto());

        this.mockMvc.perform(post("/account")
                        .param("hiddenAction", "changePassword")
                        .param("username", changedPasswordDto.getUsername())
                        .param("password", changedPasswordDto.getPassword())
                        .param("newPassword", changedPasswordDto.getNewPassword())
                        .param("confirmNewPassword", changedPasswordDto.getConfirmNewPassword()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors
                        ("changedPassword", "password", "confirmNewPassword"))
                .andExpect(model().attributeExists("openChangePassword"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("userInformation"))
                .andExpect(model().attributeExists("changedUsername"))
                .andExpect(model().attributeExists("changedPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));

        verify(accountService, never()).saveNewPassword(changedPasswordDto);
        verify(ticketService, times(1)).getUserTickets(MOCK_USERNAME);
        verify(accountService, times(1)).getUserInformation(MOCK_USERNAME);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(ticketService);
    }
}
