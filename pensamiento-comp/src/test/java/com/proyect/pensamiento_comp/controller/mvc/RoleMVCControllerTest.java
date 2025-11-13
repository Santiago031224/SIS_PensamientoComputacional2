package com.proyect.pensamiento_comp.controller.mvc;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.services.PermissionService;
import com.proyect.pensamiento_comp.services.RoleService;
import com.proyect.pensamiento_comp.filters.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RoleMVCController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@Import({RoleMVCControllerTest.MockBeansConfig.class})
class RoleMVCControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        public RoleService roleService() {
            return Mockito.mock(RoleService.class);
        }
        @Bean
        public PermissionService permissionService() {
            return Mockito.mock(PermissionService.class);
        }
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void testListRoles_ReturnsView() throws Exception {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");
        Permission perm = new Permission();
        perm.setId(2L);
        perm.setName("PERM_READ");
        Mockito.when(roleService.findAllEntities()).thenReturn(Collections.singletonList(role));
        Mockito.when(permissionService.findAllEntities()).thenReturn(Collections.singletonList(perm));

        mockMvc.perform(get("/mvc/roles"))
                .andExpect(status().isOk())
                .andExpect(view().name("roles/list"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("permissions"));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void testAddRoleForm_ReturnsView() throws Exception {
        Permission perm = new Permission();
        perm.setId(2L);
        perm.setName("PERM_READ");
        Mockito.when(permissionService.findAllEntities()).thenReturn(Collections.singletonList(perm));

        mockMvc.perform(get("/mvc/roles/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("roles/add"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attributeExists("permissions"));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void testAddRole_Post_Redirects() throws Exception {
        Mockito.when(roleService.createEntity(any(RoleCreateDTO.class))).thenReturn(null); // Si el método es void, usa thenReturn(null)
        mockMvc.perform(post("/mvc/roles/add")
                .param("name", "ROLE_USER")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/roles"));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void testAddPermissionToRole_Post_Redirects() throws Exception {
        Mockito.doNothing().when(roleService).addPermissionToRole(eq(1L), eq(2L));
        mockMvc.perform(post("/mvc/roles/1/add-permission")
                .param("permissionId", "2")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/roles"));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void testDeleteRole_Redirects() throws Exception {
        Mockito.doNothing().when(roleService).deleteEntity(3L);
        mockMvc.perform(get("/mvc/roles/delete?id=3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/roles"));
    }
}
