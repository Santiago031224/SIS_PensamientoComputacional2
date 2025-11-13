package com.proyect.pensamiento_comp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PeriodCreateDTO {
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
}
