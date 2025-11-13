package com.proyect.pensamiento_comp.security;

import com.proyect.pensamiento_comp.config.SecurityConfig;
import com.proyect.pensamiento_comp.filters.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void passwordEncoder_isPlainOrBcrypt() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof PlainOrBcryptPasswordEncoder);

        String raw = "secret";
        String encoded = encoder.encode(raw);

        assertTrue(encoder.matches(raw, encoded), "El encoder debe validar su propio hash");
    }


    @Test
    void authenticationManager_loads() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(configuration.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager am = securityConfig.authenticationManager(configuration);
        assertSame(mockManager, am);
    }

    @Test
    void apiSecurity_registersJwtFilterBeforeUsernamePassword() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        SecurityFilterChain chain = securityConfig.apiSecurity(http);
        verify(http, atLeastOnce()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Test
    void mvcSecurity_buildsChain_withoutErrors() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityFilterChain chain = securityConfig.mvcSecurity(http);

        assertTrue(true);
    }
}
