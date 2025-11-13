package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.TeachingAssignmentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import java.util.List;

public interface TeachingAssignmentService {

    List<TeachingAssignmentResponseDTO> findAll();

    TeachingAssignmentResponseDTO findById(Long id);

    TeachingAssignmentResponseDTO create(TeachingAssignmentCreateDTO dto);

    TeachingAssignmentResponseDTO update(Long id, TeachingAssignmentCreateDTO dto);

    void deleteById(Long id);
}
