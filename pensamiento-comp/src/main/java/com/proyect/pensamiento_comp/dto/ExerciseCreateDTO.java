package com.proyect.pensamiento_comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseCreateDTO {
    private Long id; 
    private String title;
    private String description;
    private String difficulty;
    private String programming_language;
}
