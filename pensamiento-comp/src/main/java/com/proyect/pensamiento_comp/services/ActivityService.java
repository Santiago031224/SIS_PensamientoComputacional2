package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.ActivityCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityResponseDTO;
import java.util.List;

public interface ActivityService {
    List<ActivityResponseDTO> findAll();
    ActivityResponseDTO findById(Long id);
    ActivityResponseDTO create(ActivityCreateDTO dto);
    ActivityResponseDTO update(Long id, ActivityCreateDTO dto);
    void delete(Long id);
}
