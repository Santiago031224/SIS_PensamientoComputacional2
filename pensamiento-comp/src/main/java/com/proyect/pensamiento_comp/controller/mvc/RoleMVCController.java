package com.proyect.pensamiento_comp.controller.mvc;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.services.PermissionService;
import com.proyect.pensamiento_comp.services.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mvc/roles")
@RequiredArgsConstructor
public class RoleMVCController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('adminpanel.manage_roles') or hasRole('ADMIN')")
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAllEntities();
        model.addAttribute("roles", roles);
        List<Permission> permissions = permissionService.findAllEntities();
        model.addAttribute("permissions", permissions);
        return "roles/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('adminpanel.manage_roles') or hasRole('ADMIN')")
    public String addRoleForm(Model model) {
        RoleCreateDTO role = new RoleCreateDTO();
        model.addAttribute("role", role);
        List<Permission> permissions = permissionService.findAllEntities();
        model.addAttribute("permissions", permissions);
        return "roles/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('adminpanel.manage_roles') or hasRole('ADMIN')")
    public String addRole(@ModelAttribute RoleCreateDTO role) {
        roleService.createEntity(role);
        return "redirect:/mvc/roles";
    }

    @PostMapping("/{roleId}/add-permission")
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String addPermissionToRole(@PathVariable Long roleId, @RequestParam Long permissionId) {
        roleService.addPermissionToRole(roleId, permissionId);
        return "redirect:/mvc/roles";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('adminpanel.manage_roles') or hasRole('ADMIN')")
    public String deleteRole(@RequestParam Long id) {
        roleService.deleteEntity(id);
        return "redirect:/mvc/roles";
    }
}
