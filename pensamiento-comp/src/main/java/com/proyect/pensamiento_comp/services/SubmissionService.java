package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.SubmissionCreateDTO;
import com.proyect.pensamiento_comp.dto.response.SubmissionResponseDTO;
import java.util.List;

public interface SubmissionService {

    List<SubmissionResponseDTO> findAll();

    SubmissionResponseDTO findById(Long id);

    SubmissionResponseDTO create(SubmissionCreateDTO dto);

    SubmissionResponseDTO update(Long id, SubmissionCreateDTO dto);

    void deleteById(Long id);
}
