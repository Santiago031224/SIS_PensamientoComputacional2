package com.proyect.pensamiento_comp.security;

import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private JwtService jwtService;

    @Test
    void extractToken_okAndMissing() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer abc.def.ghi");
        assertEquals("abc.def.ghi", jwtService.extractToken(req));

        when(req.getHeader("Authorization")).thenReturn(null);
        assertNull(jwtService.extractToken(req));
    }

    @Test
    void validateToken_delegates() {
        when(jwtUtil.validateToken("tkn")).thenReturn(true);
        assertTrue(jwtService.validateToken("tkn"));
        assertFalse(jwtService.validateToken(null));
    }

    @Test
    void getAuthentication_ok() {
        Claims claims = mock(Claims.class);
        when(jwtUtil.getClaims("tkn")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("user@ex.com");

        User u = new User();
        u.setId(1L);
        u.setEmail("user@ex.com");

        Role r = new Role();
        r.setName("ADMIN");
        u.setRoles(Set.of(r));

        // Mock para los métodos reales que usa JwtService
        when(userRepository.findByEmail("user@ex.com")).thenReturn(Optional.of(u));
        when(userRepository.findByIdWithRoles(1L)).thenReturn(Optional.of(u));

        Authentication auth = jwtService.getAuthentication("tkn");

        assertNotNull(auth, "Debe devolver un objeto Authentication");
        assertEquals(u, auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }


    @Test
    void getAuthentication_userNotFound() {
        Claims claims = mock(Claims.class);
        when(jwtUtil.getClaims("tkn")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("missing@ex.com");

        when(userRepository.findByEmail("missing@ex.com")).thenReturn(Optional.empty());

        assertNull(jwtService.getAuthentication("tkn"));
    }

}

