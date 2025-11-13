package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.PointCodeCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PointCodeResponseDTO;
import com.proyect.pensamiento_comp.services.PointCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pointcodes")
@RequiredArgsConstructor
public class PointCodeRestController {

    private final PointCodeService service;

    @GetMapping
    public ResponseEntity<List<PointCodeResponseDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getById(@PathVariable String code) {
        try {
            return new ResponseEntity<>(service.findById(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("PointCode not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PointCodeCreateDTO dto) {
        try {
            PointCodeResponseDTO response = service.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating PointCode", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable String code, @RequestBody PointCodeCreateDTO dto) {
        try {
            PointCodeResponseDTO response = service.update(code, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating PointCode", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> delete(@PathVariable String code) {
        try {
            service.delete(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting PointCode", HttpStatus.BAD_REQUEST);
        }
    }
}
