package com.proyect.pensamiento_comp.dto;

import lombok.Data;

@Data
public class ActivityExerciseCreateDTO {
    private Long activityId;
    private Long exerciseId;
    private Integer position;
    private Integer pointValue; // Puntos que vale el ejercicio en la actividad
}
