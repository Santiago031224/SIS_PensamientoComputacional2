package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfessorResponseDTO {
    private Long id;
    private LocalDate registrationDate;
    private String officeLocation;
    private String specialtyArea;
    private Integer maxGroupsAllowed;
    private String academicRank;
    private Double ratingAverage;
    private Long userId;
}
