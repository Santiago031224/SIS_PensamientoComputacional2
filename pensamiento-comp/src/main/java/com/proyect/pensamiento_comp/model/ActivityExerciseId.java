package com.proyect.pensamiento_comp.model;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ActivityExerciseId implements Serializable {

    @Column(name = "ACTIVITY_id")
    private Long activityId;

    @Column(name = "EXERCISE_id")
    private Long exerciseId;
}

