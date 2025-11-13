package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.dto.response.PermissionResponseDTO;
import com.proyect.pensamiento_comp.model.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    @Mapping(target = "id", ignore = true)
    Permission toEntity(PermissionDTO dto);

    PermissionResponseDTO toDto(Permission entity);

    List<PermissionResponseDTO> toDtoList(List<Permission> entities);
}
