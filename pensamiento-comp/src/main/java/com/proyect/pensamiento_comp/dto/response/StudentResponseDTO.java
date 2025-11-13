package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;

@Data
public class StudentResponseDTO {
    private Long id;
    private String code;
    private Double gpa;
    private Long userId;
    private Long levelId;
}
