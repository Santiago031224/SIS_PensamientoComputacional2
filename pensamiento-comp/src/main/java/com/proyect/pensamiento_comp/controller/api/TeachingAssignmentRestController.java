package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.TeachingAssignmentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import com.proyect.pensamiento_comp.services.TeachingAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/teaching-assignments")
@RequiredArgsConstructor
public class TeachingAssignmentRestController {

    private final TeachingAssignmentService service;

    @GetMapping
    public ResponseEntity<?> getAll() {
        log.info("📥 GET /api/teaching-assignments - Iniciando petición");
        log.info("🔍 [CONTROLLER] Llamando a service.findAll()...");
        try {
            List<TeachingAssignmentResponseDTO> result = service.findAll();
            log.info("✅ [CONTROLLER] service.findAll() completado exitosamente");
            log.info("✅ GET /api/teaching-assignments - Retornando {} registros", result.size());
            log.debug("📋 Datos: {}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("❌ [CONTROLLER] Exception capturada en getAll()");
            log.error("❌ Tipo de excepción: {}", e.getClass().getName());
            log.error("❌ Mensaje: {}", e.getMessage());
            log.error("❌ GET /api/teaching-assignments - Error: {}", e.getMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>("Error fetching teaching assignments: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("TeachingAssignment not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TeachingAssignmentCreateDTO dto) {
        try {
            TeachingAssignmentResponseDTO response = service.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating TeachingAssignment", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TeachingAssignmentCreateDTO dto) {
        try {
            TeachingAssignmentResponseDTO response = service.update(id, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating TeachingAssignment", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting TeachingAssignment", HttpStatus.BAD_REQUEST);
        }
    }
}