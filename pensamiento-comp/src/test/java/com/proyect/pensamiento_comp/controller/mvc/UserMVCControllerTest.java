package com.proyect.pensamiento_comp.controller.mvc;

import com.proyect.pensamiento_comp.dto.UserCreateDTO;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.services.RoleService;
import com.proyect.pensamiento_comp.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserMVCController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.proyect.pensamiento_comp.filters.JwtAuthenticationFilter.class))
@Import(UserMVCControllerTest.MockBeansConfig.class)
class UserMVCControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
        @Bean
        public RoleService roleService() {
            return Mockito.mock(RoleService.class);
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"READ_USER", "WRITE_USER"})
    void testGetAll_ReturnsView() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("admin");
        Role role = new Role();
        role.setId(2L);
        role.setName("ROLE_ADMIN");
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(roleService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/mvc/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"WRITE_USER"})
    void testAddUserForm_ReturnsView() throws Exception {

        com.proyect.pensamiento_comp.dto.response.RoleResponseDTO role = new com.proyect.pensamiento_comp.dto.response.RoleResponseDTO();
        role.setId(2L);
        role.setName("ROLE_ADMIN");
        Mockito.when(roleService.findAll()).thenReturn(Collections.singletonList(role));

        mockMvc.perform(get("/mvc/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/add"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"WRITE_USER"})
    void testAddUser_Post_Redirects() throws Exception {
        Mockito.when(userService.create(any(UserCreateDTO.class))).thenReturn(Mockito.mock(com.proyect.pensamiento_comp.dto.response.UserResponseDTO.class));
        mockMvc.perform(post("/mvc/users/add")
                .param("username", "newuser")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/users"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"DELETE_USER"})
    void testToggleUserStatus_Post_Redirects() throws Exception {
        Mockito.doNothing().when(userService).toggleUserStatus(1L);
        mockMvc.perform(post("/mvc/users/toggle-status")
                .param("id", "1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddRoleToUser_Post_Redirects() throws Exception {
        Mockito.when(userService.addRoleToUser(1L, 2L)).thenReturn(Mockito.mock(com.proyect.pensamiento_comp.dto.response.UserResponseDTO.class));
        mockMvc.perform(post("/mvc/users/1/add-role")
                .param("roleId", "2")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRemoveRoleFromUser_Post_Redirects() throws Exception {
        Mockito.when(userService.removeRoleFromUser(1L, 2L)).thenReturn(Mockito.mock(com.proyect.pensamiento_comp.dto.response.UserResponseDTO.class));
        mockMvc.perform(post("/mvc/users/1/remove-role")
                .param("roleId", "2")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/users"));
    }
}
