package com.proyect.pensamiento_comp.dto.response;

import lombok.Data;

@Data
public class ActivityExerciseResponseDTO {
    private Long activityId;
    private Long exerciseId;
    private Integer position;
    private Integer pointValue; // Puntos que vale el ejercicio en la actividad
}
