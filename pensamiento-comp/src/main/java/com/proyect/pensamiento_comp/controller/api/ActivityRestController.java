package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.ActivityCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityResponseDTO;
import com.proyect.pensamiento_comp.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityRestController {

    private final ActivityService service;

    @GetMapping
    @PreAuthorize("hasAuthority('activity.view_current') or hasAuthority('activity.view_past') or hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<ActivityResponseDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('activity.view_current') or hasAuthority('activity.view_past') or hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Activity not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('activity.create') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> create(@RequestBody ActivityCreateDTO dto) {
        try {
            ActivityResponseDTO response = service.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            return new ResponseEntity<>("Error creating Activity: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('activity.edit') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ActivityCreateDTO dto) {
        try {
            ActivityResponseDTO response = service.update(id, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Activity", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('activity.delete') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting Activity", HttpStatus.BAD_REQUEST);
        }
    }
}