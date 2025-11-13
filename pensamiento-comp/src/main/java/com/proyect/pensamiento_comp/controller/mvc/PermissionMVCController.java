package com.proyect.pensamiento_comp.controller.mvc;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc/permissions")
@RequiredArgsConstructor
public class PermissionMVCController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.findAllEntities());
        return "permissions/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String addPermissionForm(Model model) {
        model.addAttribute("permission", new PermissionDTO());
        return "permissions/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String addPermission(@ModelAttribute PermissionDTO dto) {
        permissionService.createEntity(dto);
        return "redirect:/mvc/permissions";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String editPermissionForm(@PathVariable Long id, Model model) {
        Permission permission = permissionService.findAllEntities()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        PermissionDTO dto = new PermissionDTO();
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        model.addAttribute("permission", dto);
        model.addAttribute("id", id);
        return "permissions/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String editPermission(@PathVariable Long id, @ModelAttribute PermissionDTO dto) {
        permissionService.updateEntity(id, dto);
        return "redirect:/mvc/permissions";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('adminpanel.manage_permissions') or hasRole('ADMIN')")
    public String deletePermission(@RequestParam Long id, Model model) {
        try {
            permissionService.deleteEntity(id);
            return "redirect:/mvc/permissions";
        } catch (Exception e) {
            model.addAttribute("permissions", permissionService.findAllEntities());
            model.addAttribute("errorMessage", "No se pudo eliminar el permiso. Es posible que esté en uso o no exista.");
            return "permissions/list";
        }
    }

}
