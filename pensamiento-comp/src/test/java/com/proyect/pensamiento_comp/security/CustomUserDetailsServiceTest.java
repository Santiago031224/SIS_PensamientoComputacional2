package com.proyect.pensamiento_comp.security;

import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void loadUserByUsername_found() {
        User u = new User();
        u.setEmail("user@ex.com");
        u.setPassword("enc");
        when(userRepository.findByEmail("user@ex.com")).thenReturn(Optional.of(u));

        UserDetails details = service.loadUserByUsername("user@ex.com");
        assertNotNull(details);
        assertEquals("user@ex.com", details.getUsername());
        assertEquals("enc", details.getPassword());
    }

    @Test
    void loadUserByUsername_notFound() {
        when(userRepository.findByEmail("no@ex.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("no@ex.com"));
    }
}

