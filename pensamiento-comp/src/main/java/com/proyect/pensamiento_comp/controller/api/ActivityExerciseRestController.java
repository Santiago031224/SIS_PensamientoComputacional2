package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.ActivityExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityExerciseResponseDTO;
import com.proyect.pensamiento_comp.services.Impl.ActivityExerciseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// This controller is obsolete as Activity-Exercise relationships are embedded in ActivityDocument
@RequestMapping("/api/activity-exercises")
@RequiredArgsConstructor
public class ActivityExerciseRestController {

    private final ActivityExerciseServiceImpl service;

    @GetMapping
    public ResponseEntity<List<ActivityExerciseResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{activityId}/{exerciseId}")
    public ResponseEntity<?> getByIds(@PathVariable Long activityId, @PathVariable Long exerciseId) {
        try {
            return ResponseEntity.ok(service.findByIds(activityId, exerciseId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ActivityExercise not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ActivityExerciseCreateDTO dto) {
        try {
            return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating ActivityExercise");
        }
    }

    @PutMapping("/{activityId}/{exerciseId}")
    public ResponseEntity<?> update(
            @PathVariable Long activityId,
            @PathVariable Long exerciseId,
            @RequestBody ActivityExerciseCreateDTO dto) {
        try {
            return ResponseEntity.ok(service.update(activityId, exerciseId, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating ActivityExercise");
        }
    }

    @DeleteMapping("/{activityId}/{exerciseId}")
    public ResponseEntity<?> delete(
            @PathVariable Long activityId,
            @PathVariable Long exerciseId) {
        try {
            service.delete(activityId, exerciseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting ActivityExercise");
        }
    }
}
