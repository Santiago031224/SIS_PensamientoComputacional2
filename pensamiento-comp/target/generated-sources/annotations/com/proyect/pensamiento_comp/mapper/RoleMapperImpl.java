package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PermissionResponseDTO;
import com.proyect.pensamiento_comp.dto.response.RoleResponseDTO;
import com.proyect.pensamiento_comp.model.Permission;
import com.proyect.pensamiento_comp.model.Role;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T21:33:45-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toEntity(RoleCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();

        role.setName( dto.getName() );

        return role;
    }

    @Override
    public RoleResponseDTO toDto(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();

        roleResponseDTO.setId( entity.getId() );
        roleResponseDTO.setName( entity.getName() );
        roleResponseDTO.setPermissions( permissionSetToPermissionResponseDTOSet( entity.getPermissions() ) );

        return roleResponseDTO;
    }

    @Override
    public List<RoleResponseDTO> toDtoList(List<Role> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RoleResponseDTO> list = new ArrayList<RoleResponseDTO>( entities.size() );
        for ( Role role : entities ) {
            list.add( toDto( role ) );
        }

        return list;
    }

    protected PermissionResponseDTO permissionToPermissionResponseDTO(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();

        permissionResponseDTO.setId( permission.getId() );
        permissionResponseDTO.setName( permission.getName() );
        permissionResponseDTO.setDescription( permission.getDescription() );

        return permissionResponseDTO;
    }

    protected Set<PermissionResponseDTO> permissionSetToPermissionResponseDTOSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionResponseDTO> set1 = new LinkedHashSet<PermissionResponseDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( permissionToPermissionResponseDTO( permission ) );
        }

        return set1;
    }
}
