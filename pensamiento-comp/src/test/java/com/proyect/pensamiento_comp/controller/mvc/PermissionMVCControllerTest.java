// java
package com.proyect.pensamiento_comp.controller.mvc;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.services.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = PermissionMVCController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = com.proyect.pensamiento_comp.filters.JwtAuthenticationFilter.class
        )
)
@Import(PermissionMVCControllerTest.MockBeansConfig.class)
class PermissionMVCControllerTest {

    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        PermissionService permissionService() {
            return Mockito.mock(PermissionService.class);
        }
        static class StubViewResolver implements ViewResolver, Ordered {
            @Override
            public View resolveViewName(String viewName, Locale locale) {
                if (viewName == null) return null;
                if (viewName.startsWith("redirect:") || viewName.startsWith("forward:")) {
                    return null;
                }
                return new AbstractView() {
                    @Override
                    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {

                    }
                };
            }
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }
        }
        @Bean
        ViewResolver noopViewResolver() {
            return new StubViewResolver();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PermissionService permissionService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(permissionService);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testListPermissions_ReturnsView() throws Exception {
        // given
        Permission p = new Permission();
        p.setId(1L);
        p.setName("READ_USER");
        Mockito.when(permissionService.findAllEntities()).thenReturn(List.of(p));


        mockMvc.perform(get("/mvc/permissions"))
                .andExpect(status().isOk())
                .andExpect(view().name("permissions/list"))
                .andExpect(model().attributeExists("permissions"));


        Mockito.verify(permissionService, times(1)).findAllEntities();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testAddPermissionForm_ReturnsView() throws Exception {

        mockMvc.perform(get("/mvc/permissions/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("permissions/add"))
                .andExpect(model().attributeExists("permission"));

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testAddPermission_Redirects() throws Exception {

        Mockito.when(permissionService.createEntity(any(PermissionDTO.class)))
                .thenReturn(new Permission());


        mockMvc.perform(post("/mvc/permissions/add")
                        .param("name", "WRITE_USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/permissions"));


        ArgumentCaptor<PermissionDTO> captor = ArgumentCaptor.forClass(PermissionDTO.class);
        Mockito.verify(permissionService, times(1)).createEntity(captor.capture());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testEditPermissionForm_ReturnsView() throws Exception {

        Permission existing = new Permission();
        existing.setId(1L);
        existing.setName("READ_USER");
        Mockito.when(permissionService.findAllEntities()).thenReturn(List.of(existing));


        mockMvc.perform(get("/mvc/permissions/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("permissions/edit"))
                .andExpect(model().attributeExists("permission"));


        Mockito.verify(permissionService, times(1)).findAllEntities();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testEditPermission_Redirects() throws Exception {

        Mockito.when(permissionService.updateEntity(eq(1L), any(PermissionDTO.class)))
                .thenReturn(new Permission());


        mockMvc.perform(post("/mvc/permissions/edit/1")
                        .param("name", "WRITE_USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/permissions"));


        Mockito.verify(permissionService, times(1))
                .updateEntity(eq(1L), any(PermissionDTO.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeletePermission_Success() throws Exception {

        Mockito.doNothing().when(permissionService).deleteEntity(1L);


        mockMvc.perform(get("/mvc/permissions/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/permissions"));


        Mockito.verify(permissionService, times(1)).deleteEntity(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeletePermission_Failure() throws Exception {

        Mockito.doThrow(new RuntimeException("delete failed"))
                .when(permissionService).deleteEntity(2L);
        Mockito.when(permissionService.findAllEntities()).thenReturn(List.of());


        mockMvc.perform(get("/mvc/permissions/delete").param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("permissions/list"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("permissions"));


        Mockito.verify(permissionService, times(1)).deleteEntity(2L);
        Mockito.verify(permissionService, Mockito.atLeastOnce()).findAllEntities();
    }
}
