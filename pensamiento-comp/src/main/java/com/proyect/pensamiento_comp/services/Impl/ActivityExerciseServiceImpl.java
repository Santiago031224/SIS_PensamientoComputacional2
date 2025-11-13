package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.documents.ExerciseDocument;
import com.proyect.pensamiento_comp.documents.repository.ActivityDocumentRepository;
import com.proyect.pensamiento_comp.documents.repository.ExerciseDocumentRepository;
import com.proyect.pensamiento_comp.dto.ActivityExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityExerciseResponseDTO;
import com.proyect.pensamiento_comp.mapper.ActivityExerciseMapper;
import com.proyect.pensamiento_comp.services.ActivityExerciseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityExerciseServiceImpl implements ActivityExerciseService {

    private final ActivityDocumentRepository activityRepository;
    private final ExerciseDocumentRepository exerciseRepository;
    private final ActivityExerciseMapper mapper;
    private final Logger logger = Logger.getLogger(ActivityExerciseServiceImpl.class.getName());

    @Override
    public List<ActivityExerciseResponseDTO> findAll() {
        logger.info("Fetching all ActivityExercises from MongoDB (embedded in activities)");
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getExercises() != null)
                .flatMap(activity -> activity.getExercises().stream()
                        .map(ex -> {
                            ActivityExerciseResponseDTO dto = new ActivityExerciseResponseDTO();
                            dto.setActivityId(activity.getRelationalId());
                            dto.setExerciseId(ex.getExerciseId());
                            dto.setPosition(ex.getPosition());
                            dto.setPointValue(ex.getPointValue()); // Puntos del ejercicio
                            return dto;
                        }))
                .collect(Collectors.toList());
    }

    @Override
    public ActivityExerciseResponseDTO findById(Long id) {
        throw new UnsupportedOperationException("Use composite key to fetch ActivityExercise");
    }

    public ActivityExerciseResponseDTO findByIds(Long activityId, Long exerciseId) {
        logger.info("Fetching ActivityExercise by composite key from MongoDB");
        ActivityDocument activity = activityRepository.findByRelationalId(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
        
        if (activity.getExercises() == null) {
            throw new EntityNotFoundException("No exercises in this activity");
        }
        
        ActivityDocument.ExerciseEmbed exerciseEmbed = activity.getExercises().stream()
                .filter(ex -> ex.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found in activity"));
        
        ActivityExerciseResponseDTO dto = new ActivityExerciseResponseDTO();
        dto.setActivityId(activityId);
        dto.setExerciseId(exerciseId);
        dto.setPosition(exerciseEmbed.getPosition());
        dto.setPointValue(exerciseEmbed.getPointValue()); // Puntos del ejercicio
        return dto;
    }

    @Override
    public ActivityExerciseResponseDTO create(ActivityExerciseCreateDTO dto) {
        logger.info("Creating ActivityExercise in MongoDB (embedding in activity)");

        ActivityDocument activity = activityRepository.findByRelationalId(dto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
        
        ExerciseDocument exercise = exerciseRepository.findById(dto.getExerciseId().toString())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (activity.getExercises() == null) {
            activity.setExercises(new ArrayList<>());
        }
        
        ActivityDocument.ExerciseEmbed exerciseEmbed = new ActivityDocument.ExerciseEmbed();
        exerciseEmbed.setExerciseId(dto.getExerciseId());
        exerciseEmbed.setDescription(exercise.getDescription());
        exerciseEmbed.setDifficulty(exercise.getDifficulty());
        exerciseEmbed.setPosition(dto.getPosition());
        exerciseEmbed.setPointValue(dto.getPointValue()); // Puntos del ejercicio
        
        activity.getExercises().add(exerciseEmbed);
        activityRepository.save(activity);
        
        ActivityExerciseResponseDTO response = new ActivityExerciseResponseDTO();
        response.setActivityId(dto.getActivityId());
        response.setExerciseId(dto.getExerciseId());
        response.setPosition(dto.getPosition());
        response.setPointValue(dto.getPointValue()); // Puntos en respuesta
        return response;
    }

    @Override
    public ActivityExerciseResponseDTO update(Long id, ActivityExerciseCreateDTO dto) {
        throw new UnsupportedOperationException("Use composite key to update ActivityExercise");
    }

    public ActivityExerciseResponseDTO update(Long activityId, Long exerciseId, ActivityExerciseCreateDTO dto) {
        logger.info("Updating ActivityExercise position in MongoDB");

        ActivityDocument activity = activityRepository.findByRelationalId(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
        
        if (activity.getExercises() == null) {
            throw new EntityNotFoundException("No exercises in this activity");
        }
        
        ActivityDocument.ExerciseEmbed exerciseEmbed = activity.getExercises().stream()
                .filter(ex -> ex.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found in activity"));
        
        exerciseEmbed.setPosition(dto.getPosition());
        exerciseEmbed.setPointValue(dto.getPointValue()); // Actualizar puntos
        activityRepository.save(activity);
        
        ActivityExerciseResponseDTO response = new ActivityExerciseResponseDTO();
        response.setActivityId(activityId);
        response.setExerciseId(exerciseId);
        response.setPosition(dto.getPosition());
        response.setPointValue(dto.getPointValue()); // Puntos en respuesta
        return response;
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Use composite key to delete ActivityExercise");
    }

    public void delete(Long activityId, Long exerciseId) {
        logger.info("Deleting ActivityExercise from MongoDB (removing from activity)");
        
        ActivityDocument activity = activityRepository.findByRelationalId(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
        
        if (activity.getExercises() == null) {
            throw new EntityNotFoundException("No exercises in this activity");
        }
        
        boolean removed = activity.getExercises().removeIf(ex -> ex.getExerciseId().equals(exerciseId));
        
        if (!removed) {
            throw new EntityNotFoundException("Exercise not found in activity");
        }
        
        activityRepository.save(activity);
    }
}
