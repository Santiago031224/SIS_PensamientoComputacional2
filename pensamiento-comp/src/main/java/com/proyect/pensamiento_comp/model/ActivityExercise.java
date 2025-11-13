package com.proyect.pensamiento_comp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * Activity and Exercise are now in MongoDB, this relation is managed there.
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - ActivityExercise relation is now in MongoDB only
// @Table(name = "ACTIVITY_EXERCISE")
public class ActivityExercise {

    @EmbeddedId
    private ActivityExerciseId id;

    private Integer position;

    @ManyToOne
    @MapsId("activityId") // referencia al campo
    @JoinColumn(name = "ACTIVITY_id")
    private Activity activity;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "EXERCISE_id")
    private Exercise exercise;
}

