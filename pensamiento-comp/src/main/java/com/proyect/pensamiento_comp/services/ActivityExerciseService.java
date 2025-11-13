package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.ActivityExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityExerciseResponseDTO;

import java.util.List;

public interface ActivityExerciseService {
    List<ActivityExerciseResponseDTO> findAll();
    ActivityExerciseResponseDTO findById(Long id);
    ActivityExerciseResponseDTO create(ActivityExerciseCreateDTO dto);
    ActivityExerciseResponseDTO update(Long id, ActivityExerciseCreateDTO dto);
    void delete(Long id);
}
