package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.ProfessorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ProfessorResponseDTO;
import java.util.List;

public interface ProfessorService {

    List<ProfessorResponseDTO> findAll();

    ProfessorResponseDTO findById(Long id);

    ProfessorResponseDTO create(ProfessorCreateDTO dto);

    ProfessorResponseDTO update(Long id, ProfessorCreateDTO dto);

    void deleteById(Long id);
}
