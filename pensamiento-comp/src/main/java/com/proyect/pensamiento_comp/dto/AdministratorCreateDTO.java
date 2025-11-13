package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorCreateDTO {
    private String department;
    private Long userId;
}
