package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.dto.response.PermissionResponseDTO;
import com.proyect.pensamiento_comp.model.Permission;
import java.util.List;

public interface PermissionService {


    List<PermissionResponseDTO> findAll();
    PermissionResponseDTO findById(Long id);
    PermissionResponseDTO create(PermissionDTO dto);
    PermissionResponseDTO update(Long id, PermissionDTO dto);
    void delete(Long id);


    List<Permission> findAllEntities();
    Permission createEntity(PermissionDTO dto);
    Permission updateEntity(Long id, PermissionDTO dto);
    void deleteEntity(Long id);
}
