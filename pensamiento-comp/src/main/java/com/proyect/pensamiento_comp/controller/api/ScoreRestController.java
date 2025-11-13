package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.ScoreCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ScoreResponseDTO;
import com.proyect.pensamiento_comp.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreRestController {

    private final ScoreService service;

    @GetMapping
    @PreAuthorize("hasAuthority('score.view_group') or hasAuthority('score.view_personal') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<List<ScoreResponseDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('score.view_group') or hasAuthority('score.view_personal') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Score not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('score.assign') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> create(@RequestBody ScoreCreateDTO dto) {
        try {
            ScoreResponseDTO response = service.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating Score", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('score.edit') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ScoreCreateDTO dto) {
        try {
            ScoreResponseDTO response = service.update(id, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Score", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('score.edit') or hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting Score", HttpStatus.BAD_REQUEST);
        }
    }
}