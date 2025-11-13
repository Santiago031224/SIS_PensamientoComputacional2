package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.PeriodCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PeriodResponseDTO;
import java.util.List;

public interface PeriodService {
    List<PeriodResponseDTO> findAll();
    PeriodResponseDTO findById(Long id);
    PeriodResponseDTO create(PeriodCreateDTO dto);
    PeriodResponseDTO update(Long id, PeriodCreateDTO dto);
    void delete(Long id);
}
