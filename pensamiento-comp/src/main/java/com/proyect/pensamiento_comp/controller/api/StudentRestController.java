package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.StudentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.StudentResponseDTO;
import com.proyect.pensamiento_comp.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.proyect.pensamiento_comp.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService service;

    @GetMapping
    @PreAuthorize("hasAuthority('user.view_all') or hasAuthority('user.view_group_students') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<List<StudentResponseDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        }
    @GetMapping
    @PreAuthorize("hasAuthority('user.view_all') or hasAuthority('user.view_group_students') or hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<?> getMyProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Extract email from the authentication principal in a robust way.
            Object principal = authentication != null ? authentication.getPrincipal() : null;
            String email = null;

            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else if (principal instanceof User) {
                email = ((User) principal).getEmail();
            } else if (principal instanceof String) {
                // sometimes the principal is the username (email) as a String
                email = (String) principal;
            }

            // fallback to authentication.getName() if we still don't have an email
            if (email == null || email.isBlank()) {
                email = authentication != null ? authentication.getName() : null;
            }

            StudentResponseDTO student = service.findByUserEmail(email);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Student profile not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user.view_all') or hasAuthority('profile.view') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('user.view_all') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(service.findByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Student not found for user", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user.register_student') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody StudentCreateDTO dto) {
        try {
            StudentResponseDTO response = service.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating Student", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user.edit') or hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody StudentCreateDTO dto) {
        try {
            StudentResponseDTO response = service.update(id, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Student", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete') or hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting Student", HttpStatus.BAD_REQUEST);
        }
    }
}