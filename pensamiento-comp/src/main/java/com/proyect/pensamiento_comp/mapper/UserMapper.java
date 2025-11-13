package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.UserCreateDTO;
import com.proyect.pensamiento_comp.dto.response.UserResponseDTO;
import com.proyect.pensamiento_comp.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { RoleMapper.class })
public interface UserMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "status", ignore = true)
    User toEntity(UserCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    void updateEntityFromDto(UserCreateDTO dto, @MappingTarget User entity);

    UserResponseDTO toDto(User entity);

    List<UserResponseDTO> toDtoList(List<User> entities);

}
