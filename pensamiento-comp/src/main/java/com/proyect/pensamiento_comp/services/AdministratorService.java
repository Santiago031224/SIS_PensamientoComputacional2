package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.AdministratorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.AdministratorResponseDTO;
import java.util.List;

public interface AdministratorService {
    List<AdministratorResponseDTO> findAll();
    AdministratorResponseDTO findById(Long id);
    AdministratorResponseDTO create(AdministratorCreateDTO dto);
    AdministratorResponseDTO update(Long id, AdministratorCreateDTO dto);
    void delete(Long id);
}
