package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.dto.response.RoleResponseDTO;
import com.proyect.pensamiento_comp.model.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "permissions", ignore = true),
        @Mapping(target = "users", ignore = true)
    })
    Role toEntity(RoleCreateDTO dto);

    RoleResponseDTO toDto(Role entity);

    List<RoleResponseDTO> toDtoList(List<Role> entities);
}
