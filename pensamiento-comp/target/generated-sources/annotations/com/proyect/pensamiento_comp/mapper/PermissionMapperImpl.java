package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.dto.response.PermissionResponseDTO;
import com.proyect.pensamiento_comp.model.Permission;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T21:33:45-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public Permission toEntity(PermissionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Permission permission = new Permission();

        permission.setName( dto.getName() );
        permission.setDescription( dto.getDescription() );

        return permission;
    }

    @Override
    public PermissionResponseDTO toDto(Permission entity) {
        if ( entity == null ) {
            return null;
        }

        PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();

        permissionResponseDTO.setId( entity.getId() );
        permissionResponseDTO.setName( entity.getName() );
        permissionResponseDTO.setDescription( entity.getDescription() );

        return permissionResponseDTO;
    }

    @Override
    public List<PermissionResponseDTO> toDtoList(List<Permission> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PermissionResponseDTO> list = new ArrayList<PermissionResponseDTO>( entities.size() );
        for ( Permission permission : entities ) {
            list.add( toDto( permission ) );
        }

        return list;
    }
}
