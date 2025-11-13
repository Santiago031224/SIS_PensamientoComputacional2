package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateDTO {
    private String code;
    private Double gpa;
    private Long userId;
    private Long levelId; 
}
