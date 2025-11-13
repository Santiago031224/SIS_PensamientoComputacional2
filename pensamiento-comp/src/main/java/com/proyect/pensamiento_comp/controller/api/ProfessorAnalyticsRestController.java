package com.proyect.pensamiento_comp.controller.api;

import com.proyect.pensamiento_comp.dto.response.ProfessorAnalyticsDTO;
import com.proyect.pensamiento_comp.services.ProfessorAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professor/analytics")
@RequiredArgsConstructor
public class ProfessorAnalyticsRestController {

    private final ProfessorAnalyticsService professorAnalyticsService;

    /**
     * Get analytics for the currently authenticated professor
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentProfessorAnalytics() {
        try {
            ProfessorAnalyticsDTO analytics = professorAnalyticsService.getCurrentProfessorAnalytics();
            return new ResponseEntity<>(analytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error loading professor analytics: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get analytics for a specific professor by ID
     */
    @GetMapping("/{professorId}")
    public ResponseEntity<?> getProfessorAnalytics(@PathVariable Long professorId) {
        try {
            ProfessorAnalyticsDTO analytics = professorAnalyticsService.getProfessorAnalytics(professorId);
            return new ResponseEntity<>(analytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Professor not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
