package com.proyect.pensamiento_comp.controller.mvc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.proyect.pensamiento_comp.dto.UserCreateDTO;
import com.proyect.pensamiento_comp.services.RoleService;
import com.proyect.pensamiento_comp.services.UserService;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mvc/users")
@RequiredArgsConstructor
public class UserMVCController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('user.view_all') or hasRole('ADMIN')")
    public String getAll(Model model, Authentication authentication) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }

        return "users/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('user.create') or hasRole('ADMIN')")
    public String addUserForm(Model model) {
        UserCreateDTO dto = new UserCreateDTO();
        model.addAttribute("user", dto);
        model.addAttribute("roles", roleService.findAll());
        return "users/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user.create') or hasRole('ADMIN')")
    public String addUser(@ModelAttribute UserCreateDTO user) {
        userService.create(user);
        return "redirect:/mvc/users";
    }

    @PostMapping("/toggle-status")
    @PreAuthorize("hasAuthority('user.activate_deactivate') or hasRole('ADMIN')")
    public String toggleUserStatus(@RequestParam Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/mvc/users";
    }

    @PostMapping("/{userId}/add-role")
    @PreAuthorize("hasAuthority('user.assign_role') or hasRole('ADMIN')")
    public String addRoleToUser(@PathVariable Long userId, @RequestParam Long roleId) {
        userService.addRoleToUser(userId, roleId);
        return "redirect:/mvc/users";
    }

    @PostMapping("/{userId}/remove-role")
    @PreAuthorize("hasAuthority('user.assign_role') or hasRole('ADMIN')")
    public String removeRoleFromUser(@PathVariable Long userId, @RequestParam Long roleId) {
        userService.removeRoleFromUser(userId, roleId);
        return "redirect:/mvc/users";
    }
}