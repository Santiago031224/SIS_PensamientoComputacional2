package com.proyect.pensamiento_comp.services.sync;

import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.Exercise;

public interface ActivityExerciseSyncPort {
    void syncActivity(Activity activity);
    void syncExercise(Exercise exercise);
    void removeActivity(Long activityId);
    void removeExercise(Long exerciseId);
}
