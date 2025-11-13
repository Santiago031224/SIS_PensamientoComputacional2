package com.proyect.pensamiento_comp.services.Impl;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.dto.response.RoleResponseDTO;
import com.proyect.pensamiento_comp.mapper.RoleMapper;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.repository.IPermissionRepository;
import com.proyect.pensamiento_comp.repository.IRoleRepository;
import com.proyect.pensamiento_comp.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final IRoleRepository repository;
    private final IPermissionRepository permissionRepository;
    private final RoleMapper mapper;
    private final Logger logger = Logger.getLogger(RoleServiceImpl.class.getName());

    @Override
    public List<RoleResponseDTO> findAll() {
        logger.info("Fetching all roles");
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public RoleResponseDTO findById(Long id) {
        logger.info("Fetching role with id: " + id);
        Role entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return mapper.toDto(entity);
    }

    @Override
    public RoleResponseDTO create(RoleCreateDTO dto) {
        logger.info("Creating new role");
        Role entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public RoleResponseDTO update(Long id, RoleCreateDTO dto) {
        logger.info("Updating role with id: " + id);
        Role existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role updated = mapper.toEntity(dto);
        updated.setId(existing.getId());
        return mapper.toDto(repository.save(updated));
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting role with id: " + id);
        repository.deleteById(id);
    }

    @Override
    public List<Role> findAllEntities() {
        return repository.findAll();
    }

    @Override
    public Role createEntity(RoleCreateDTO dto) {
        Role entity = mapper.toEntity(dto);
        return repository.save(entity);
    }

    @Override
    public Role updateEntity(Long id, RoleCreateDTO dto) {
        Role existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role updated = mapper.toEntity(dto);
        updated.setId(existing.getId());
        return repository.save(updated);
    }

    @Override
    public void deleteEntity(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void addPermissionToRole(Long roleId, Long permissionId) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().add(permission);
        repository.save(role);
    }

}
