package com.proyect.pensamiento_comp.services.sync;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.documents.ExerciseDocument;
import com.proyect.pensamiento_comp.documents.repository.ActivityDocumentRepository;
import com.proyect.pensamiento_comp.documents.repository.ExerciseDocumentRepository;
import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.Exercise;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(value = "app.mongo.enabled", havingValue = "true")
public class MongoActivityExerciseSyncService implements ActivityExerciseSyncPort {

    private final ActivityDocumentRepository activityDocRepo;
    private final ExerciseDocumentRepository exerciseDocRepo;

    public MongoActivityExerciseSyncService(ActivityDocumentRepository activityDocRepo, ExerciseDocumentRepository exerciseDocRepo) {
        this.activityDocRepo = activityDocRepo;
        this.exerciseDocRepo = exerciseDocRepo;
    }

    @Override
    public void syncActivity(Activity activity) {
        ActivityDocument doc = activityDocRepo.findByRelationalId(activity.getId())
                .orElse(new ActivityDocument());

        doc.setRelationalId(activity.getId());
        doc.setTitle(activity.getTitle());
        doc.setDescription(activity.getDescription());
        doc.setProfessorId(activity.getProfessor() != null ? activity.getProfessor().getId() : null);
        
        // Crear el objeto Schedule si hay fechas
        if (activity.getStartDate() != null || activity.getEndDate() != null) {
            ActivityDocument.Schedule schedule = new ActivityDocument.Schedule();
            if (activity.getStartDate() != null) {
                schedule.setStartDate(activity.getStartDate().atStartOfDay());
            }
            if (activity.getEndDate() != null) {
                schedule.setEndDate(activity.getEndDate().atTime(23, 59, 59));
            }
            doc.setSchedule(schedule);
        }

        List<ActivityDocument.ExerciseEmbed> embeds = activity.getActivityExercises() == null ? List.of() :
                activity.getActivityExercises().stream()
                        .sorted(Comparator.comparingInt(ae -> ae.getPosition() == null ? Integer.MAX_VALUE : ae.getPosition()))
                        .map(ae -> new ActivityDocument.ExerciseEmbed(
                                ae.getExercise().getId(),
                                ae.getExercise().getTitle(),  // title
                                ae.getExercise().getDescription(),
                                ae.getExercise().getDifficulty(),
                                ae.getPosition(),
                                null  // pointValue - not available in legacy ActivityExercise entity
                        ))
                        .collect(Collectors.toList());
        doc.setExercises(embeds);
        activityDocRepo.save(doc);
    }

    @Override
    public void syncExercise(Exercise exercise) {
        ExerciseDocument doc = exerciseDocRepo.findByRelationalId(exercise.getId())
                .orElse(new ExerciseDocument(exercise.getId(), exercise.getTitle(), exercise.getDescription(), exercise.getDifficulty(), exercise.getProgramming_language()));
        doc.setTitle(exercise.getTitle());
        doc.setDescription(exercise.getDescription());
        doc.setDifficulty(exercise.getDifficulty());
        doc.setProgramming_language(exercise.getProgramming_language());    
        exerciseDocRepo.save(doc);
    }

    @Override
    public void removeActivity(Long activityId) {
        activityDocRepo.deleteByRelationalId(activityId);
    }

    @Override
    public void removeExercise(Long exerciseId) {
        exerciseDocRepo.deleteByRelationalId(exerciseId);
    }
}
