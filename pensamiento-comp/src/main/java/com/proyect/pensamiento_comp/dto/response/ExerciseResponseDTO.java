package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;

@Data
public class ExerciseResponseDTO {
    private Long relationalId;
    private String title;
    private String programming_language;
    private String description;
    private String difficulty;
    
    private String mongoId;}

