package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCreateDTO {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Long professorId;
}
