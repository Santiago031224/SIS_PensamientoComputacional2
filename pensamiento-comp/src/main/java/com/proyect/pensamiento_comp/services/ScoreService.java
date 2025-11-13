package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.ScoreCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ScoreResponseDTO;
import java.util.List;

public interface ScoreService {

    List<ScoreResponseDTO> findAll();

    ScoreResponseDTO findById(Long id);

    ScoreResponseDTO create(ScoreCreateDTO dto);

    ScoreResponseDTO update(Long id, ScoreCreateDTO dto);

    void deleteById(Long id);
}
