package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.dto.response.PermissionResponseDTO;
import com.proyect.pensamiento_comp.mapper.PermissionMapper;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.repository.IPermissionRepository;
import com.proyect.pensamiento_comp.repository.IRoleRepository;
import com.proyect.pensamiento_comp.services.PermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final IPermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final IRoleRepository roleRepository;
    private final Logger logger = Logger.getLogger(PermissionServiceImpl.class.getName());


    @Override
    public List<PermissionResponseDTO> findAll() {
        logger.info("Fetching all permissions (REST)");
        return permissionMapper.toDtoList(permissionRepository.findAll());
    }

    @Override
    public PermissionResponseDTO findById(Long id) {
        logger.info("Fetching permission with ID: " + id + " (REST)");
        Permission entity = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        return permissionMapper.toDto(entity);
    }

    @Override
    public PermissionResponseDTO create(PermissionDTO dto) {
        logger.info("Creating permission (REST)");
        Permission entity = permissionMapper.toEntity(dto);
        return permissionMapper.toDto(permissionRepository.save(entity));
    }

    @Override
    public PermissionResponseDTO update(Long id, PermissionDTO dto) {
        logger.info("Updating permission with ID: " + id + " (REST)");
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        Permission updated = permissionMapper.toEntity(dto);
        updated.setId(existing.getId());
        return permissionMapper.toDto(permissionRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting permission with ID: " + id + " (REST)");
        permissionRepository.deleteById(id);
    }


    public List<Permission> findAllEntities() {
        logger.info("Fetching all permissions for MVC");
        return permissionRepository.findAll();
    }

    public Permission createEntity(PermissionDTO dto) {
        logger.info("Creating permission for MVC");
        Permission entity = permissionMapper.toEntity(dto);
        return permissionRepository.save(entity);
    }

    public Permission updateEntity(Long id, PermissionDTO dto) {
        logger.info("Updating permission with ID: " + id + " for MVC");
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        Permission updated = permissionMapper.toEntity(dto);
        updated.setId(existing.getId());
        return permissionRepository.save(updated);
    }

    @Transactional
    @Override
    public void deleteEntity(Long id) {
        logger.info("Deleting permission with ID: " + id + " for MVC");

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        List<Role> roles = roleRepository.findByPermissionsContains(permission);
        for (Role role : roles) {
            role.getPermissions().remove(permission);
            roleRepository.save(role);
        }

        permissionRepository.delete(permission);
    }

}
