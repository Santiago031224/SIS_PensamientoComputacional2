package com.proyect.pensamiento_comp.dto;

import lombok.Data;

@Data
public class SubmissionCreateDTO {
    private String link;
    private String status;
    private Long studentId;
    private Long exerciseId;

}
