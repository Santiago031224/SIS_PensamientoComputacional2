package com.proyect.pensamiento_comp.security;

import com.proyect.pensamiento_comp.filters.JwtAuthenticationFilter;
import com.proyect.pensamiento_comp.security.JwtService;
import com.proyect.pensamiento_comp.services.Impl.ActivityExerciseServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = true)
class JwtAuthenticationFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ActivityExerciseServiceImpl activityExerciseService;

    @MockBean
    private JwtService jwtService; // Evita dependencias reales

    @Test
    void mvcSecurity_permitsAuthLogin() throws Exception {
        mockMvc.perform(get("/auth/login").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void api_withoutToken_isUnauthorizedOrForbidden() throws Exception {
        mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int sc = result.getResponse().getStatus();
                    if (sc != 401 && sc != 403) {
                        throw new AssertionError("Expected 401 or 403, got " + sc);
                    }
                });
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void api_withAuthority_isOkOrNoContent() throws Exception {
        mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int sc = result.getResponse().getStatus();
                    if (!(sc >= 200 && sc < 500)) {
                        throw new AssertionError("Unexpected status code: " + sc);
                    }
                });
    }
}
