package com.proyect.pensamiento_comp.controller.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import com.proyect.pensamiento_comp.security.JwtService;
import com.proyect.pensamiento_comp.security.JwtUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.Mockito;

@WebMvcTest(DashboardController.class)
@Import(DashboardControllerTest.JwtBeansTestConfig.class)
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class JwtBeansTestConfig {
        @Bean
        public JwtUtil jwtUtil() {
            return Mockito.mock(JwtUtil.class);
        }
        @Bean
        public IUserRepository userRepository() {
            return Mockito.mock(IUserRepository.class);
        }
        @Bean
        public JwtService jwtService(JwtUtil jwtUtil, IUserRepository userRepository) {
            return new JwtService(jwtUtil, userRepository);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetDashboard_AdminRedirect() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @WithMockUser(username = "prof", roles = {"PROFESSOR"})
    void testGetDashboard_ProfessorRedirect() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/dashboard"));
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void testGetDashboard_StudentRedirect() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"));
    }

    @Test
    @WithMockUser(username = "other", roles = {"OTHER"})
    void testGetDashboard_UnknownRoleRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }
}
