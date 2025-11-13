package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.ExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ExerciseResponseDTO;
import com.proyect.pensamiento_comp.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseRestController {

    private final ExerciseService service;

    @GetMapping
    @PreAuthorize("hasAuthority('exercise.view') or hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<ExerciseResponseDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('exercise.view') or hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Exercise not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('exercise.create') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> create(@RequestBody ExerciseCreateDTO dto) {
        try {
            ExerciseResponseDTO response = service.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating Exercise", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('exercise.edit') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ExerciseCreateDTO dto) {
        try {
            ExerciseResponseDTO response = service.update(id, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Exercise", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('exercise.delete') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting Exercise", HttpStatus.BAD_REQUEST);
        }
    }
}