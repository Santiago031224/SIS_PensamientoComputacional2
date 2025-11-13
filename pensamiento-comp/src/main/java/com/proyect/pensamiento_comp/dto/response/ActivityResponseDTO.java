package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ActivityResponseDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Long professorId;
    private String professorName;
    private List<ExerciseEmbedDTO> exercises;
    
    @Data
    public static class ExerciseEmbedDTO {
        private Long exerciseId;
        private String title;
        private String description;
        private String difficulty;
        private Integer position;
        private Integer pointValue;
    }
}
