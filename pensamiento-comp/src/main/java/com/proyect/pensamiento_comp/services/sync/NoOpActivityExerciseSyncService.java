package com.proyect.pensamiento_comp.services.sync;

import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.Exercise;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "app.mongo.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpActivityExerciseSyncService implements ActivityExerciseSyncPort {
    @Override
    public void syncActivity(Activity activity) {
        // no-op
    }

    @Override
    public void syncExercise(Exercise exercise) {
        // no-op
    }

    @Override
    public void removeActivity(Long activityId) {
        // no-op
    }

    @Override
    public void removeExercise(Long exerciseId) {
        // no-op
    }
}
