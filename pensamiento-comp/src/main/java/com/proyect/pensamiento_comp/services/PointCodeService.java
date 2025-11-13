package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.PointCodeCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PointCodeResponseDTO;
import java.util.List;

public interface PointCodeService {
    List<PointCodeResponseDTO> findAll();
    PointCodeResponseDTO findById(String id);
    PointCodeResponseDTO create(PointCodeCreateDTO dto);
    PointCodeResponseDTO update(String id, PointCodeCreateDTO dto);
    void delete(String id);
}
