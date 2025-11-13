package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreResponseDTO {
    private Long id;
    private Double pointsAwarded;
    private LocalDateTime assignmentDate;
    private Long submissionId;
}
