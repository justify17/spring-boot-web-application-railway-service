package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.service.AdminService;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = AdminControllerTest.MOCK_USERNAME, roles = "ADMIN")
class AdminControllerTest {

    public static final Integer MOCK_ID = 1;
    public static final String MOCK_USERNAME = "user";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestObjectFactory testObjectFactory;

    @MockBean
    private AdminService adminService;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private DepartureService departureService;

    @Test
    void whenAdmin() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("departures"))
                .andExpect(model().attributeExists("routes"))
                .andExpect(model().attributeExists("trains"))
                .andExpect(model().attributeExists("newDeparture"))
                .andExpect(model().attributeExists("openDefault"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(adminService, times(1)).getAllUsers();
        verify(adminService, times(1)).getAllRoles();
        verify(adminService, times(1)).getAllRoutes();
        verify(adminService, times(1)).getAllTrains();
        verify(departureService, times(1)).getAllDepartures();
        verifyNoMoreInteractions(adminService);
        verifyNoMoreInteractions(departureService);
    }

    @Test
    void whenChangeUserRole() throws Exception {
        String testUsername = MOCK_USERNAME;
        Integer testNewRole = MOCK_ID;

        this.mockMvc.perform(post("/admin")
                        .param("hiddenAction", "changeUserRole")
                        .param("username", testUsername)
                        .param("newRole", String.valueOf(testNewRole)))
                .andExpect(model().attributeExists("openDefault"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(adminService, times(1)).setNewUserRole(testUsername, testNewRole);
    }

    @Test
    void whenChangeAccountStatus() throws Exception {
        String testUsername = MOCK_USERNAME;

        this.mockMvc.perform(post("/admin")
                        .param("hiddenAction", "changeAccountStatus")
                        .param("username", testUsername))
                .andExpect(model().attributeExists("openDefault"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(adminService, times(1)).setNewAccountStatus(testUsername);
    }

    @Test
    void whenDeleteUser() throws Exception {
        Integer testUserId = MOCK_ID;

        this.mockMvc.perform(post("/admin")
                        .param("hiddenAction", "deleteUser")
                        .param("userId", String.valueOf(testUserId)))
                .andExpect(model().attributeExists("openDefault"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(adminService, times(1)).deleteUserById(testUserId);
    }

    @Test
    void whenCancelDeparture() throws Exception {
        Integer testDepartureId = MOCK_ID;

        this.mockMvc.perform(post("/admin")
                        .param("hiddenAction", "cancelDeparture")
                        .param("departureId", String.valueOf(testDepartureId)))
                .andExpect(model().attributeExists("openDepartures"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(departureService, times(1)).deleteDepartureById(testDepartureId);
    }

    @Test
    void whenAddNewDeparture_AndModelIsValid() throws Exception {
        DepartureDto departureDto = testObjectFactory.getValidNewDepartureDto();

        this.mockMvc.perform(post("/admin")
                        .param("hiddenAction", "createNewDeparture")
                        .param("route.id", String.valueOf(departureDto.getRoute().getId()))
                        .param("train.id", String.valueOf(departureDto.getTrain().getId()))
                        .param("departureDate", departureDto.getDepartureDate().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("openDepartures"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(departureService, times(1)).saveNewDeparture(departureDto);
    }

    @Test
    void whenAddNewDeparture_AndModelIsInvalid() throws Exception {
        DepartureDto departureDto = testObjectFactory.getInvalidNewDepartureDto();

        this.mockMvc.perform(post("/admin")
                        .param("hiddenAction", "createNewDeparture")
                        .param("route.id", String.valueOf(departureDto.getRoute().getId()))
                        .param("train.id", String.valueOf(departureDto.getTrain().getId()))
                        .param("departureDate", departureDto.getDepartureDate().toString()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("newDeparture"))
                .andExpect(model().attributeHasFieldErrors("newDeparture",
                        "route.id", "train.id", "departureDate"))
                .andExpect(model().attributeExists("openDepartures"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(departureService, never()).saveNewDeparture(departureDto);
    }

    @Test
    void whenUserOrders() throws Exception {
        String testUsername = MOCK_USERNAME;

        this.mockMvc.perform(get("/admin/userOrders")
                        .param("username", testUsername))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("userOrders"));

        verify(ticketService, times(1)).getUserTickets(testUsername);
    }

    @Test
    void whenDeleteUserOrder() throws Exception {
        Integer testTicketId = MOCK_ID;
        String testUsername = MOCK_USERNAME;

        this.mockMvc.perform(post("/admin/userOrders")
                        .param("ticketId", String.valueOf(testTicketId))
                        .param("username", testUsername))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("userOrders"));

        verify(ticketService, times(1)).deleteTicketById(testTicketId);
        verify(ticketService, times(1)).getUserTickets(testUsername);
    }

    @Test
    void whenDepartureDetails() throws Exception {
        Integer testDepartureId = MOCK_ID;

        this.mockMvc.perform(get("/admin/departureDetails")
                        .param("departureId", String.valueOf(testDepartureId)))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("departureDetails"));

        verify(ticketService, times(1)).getAllTicketsByDeparture(testDepartureId);
    }

    @Test
    void whenDeleteTicket() throws Exception {
        Integer testTicketId = MOCK_ID;
        Integer testDepartureId = MOCK_ID;

        this.mockMvc.perform(post("/admin/departureDetails")
                        .param("ticketId", String.valueOf(testTicketId))
                        .param("departureId", String.valueOf(testDepartureId)))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("departureDetails"));

        verify(ticketService, times(1)).deleteTicketById(testTicketId);
        verify(ticketService, times(1)).getAllTicketsByDeparture(testDepartureId);
    }

}
