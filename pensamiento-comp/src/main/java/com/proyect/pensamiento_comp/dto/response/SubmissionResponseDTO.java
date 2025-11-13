package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubmissionResponseDTO {
    private Long id;
    private String link;
    private LocalDateTime date;
    private String status;
    private LocalDateTime createdAt;
    private Long studentId;
    private Long exerciseId;

}
