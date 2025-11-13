package com.proyect.pensamiento_comp.services;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.dto.response.RoleResponseDTO;
import com.proyect.pensamiento_comp.model.Role;
import java.util.List;

public interface RoleService {


    List<RoleResponseDTO> findAll();
    RoleResponseDTO findById(Long id);
    RoleResponseDTO create(RoleCreateDTO dto);
    RoleResponseDTO update(Long id, RoleCreateDTO dto);
    void deleteById(Long id);


    List<Role> findAllEntities();
    Role createEntity(RoleCreateDTO dto);
    Role updateEntity(Long id, RoleCreateDTO dto);
    void deleteEntity(Long id);
    void addPermissionToRole(Long roleId, Long permissionId);
}
