package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.GroupCreateDTO;
import com.proyect.pensamiento_comp.dto.response.GroupResponseDTO;
import java.util.List;

public interface GroupService {
    List<GroupResponseDTO> findAll();
    GroupResponseDTO findById(Long id);
    GroupResponseDTO create(GroupCreateDTO dto);
    GroupResponseDTO update(Long id, GroupCreateDTO dto);
    void delete(Long id);
}
