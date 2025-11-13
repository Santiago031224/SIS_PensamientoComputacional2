package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.ExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ExerciseResponseDTO;
import java.util.List;

public interface ExerciseService {
    List<ExerciseResponseDTO> findAll();
    ExerciseResponseDTO findById(Long id);
    ExerciseResponseDTO create(ExerciseCreateDTO dto);
    ExerciseResponseDTO update(Long id, ExerciseCreateDTO dto);
    void delete(Long id);
}
