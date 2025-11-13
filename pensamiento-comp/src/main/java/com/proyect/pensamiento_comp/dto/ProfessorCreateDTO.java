package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorCreateDTO {
    private LocalDate registrationDate;
    private String officeLocation;
    private String specialtyArea;
    private Integer maxGroupsAllowed;
    private String academicRank;
    private Double ratingAverage;
    private Long userId;
}
