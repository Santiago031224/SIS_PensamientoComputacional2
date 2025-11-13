package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.LevelCreateDTO;
import com.proyect.pensamiento_comp.dto.response.LevelResponseDTO;
import java.util.List;

public interface LevelService {
    List<LevelResponseDTO> findAll();
    LevelResponseDTO findById(Long id);
    LevelResponseDTO create(LevelCreateDTO dto);
    LevelResponseDTO update(Long id, LevelCreateDTO dto);
    void delete(Long id);
}
