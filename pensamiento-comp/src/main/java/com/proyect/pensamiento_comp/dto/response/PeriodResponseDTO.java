package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PeriodResponseDTO {
    private Long id;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
}
