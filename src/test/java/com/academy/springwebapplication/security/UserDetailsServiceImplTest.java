package com.academy.springwebapplication.security;

import com.academy.springwebapplication.model.entity.Role;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void whenLoadUserByUsername_AndUserIsNotExisting() {
        String username = "notExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void whenLoadUserByUsername_AndUserIsExisting() {
        String username = "user";

        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setUsername(username);
        user.setRole(role);

        when(userRepository.findByUsername(username)).thenReturn(user);

        UserDetails result = new UserDetailsImpl(user);
        assertEquals(result, userDetailsService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }
}
