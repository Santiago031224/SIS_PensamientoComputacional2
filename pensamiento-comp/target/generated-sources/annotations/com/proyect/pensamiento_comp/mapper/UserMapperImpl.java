package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.UserCreateDTO;
import com.proyect.pensamiento_comp.dto.response.RoleResponseDTO;
import com.proyect.pensamiento_comp.dto.response.UserResponseDTO;
import com.proyect.pensamiento_comp.model.Role;
import com.proyect.pensamiento_comp.model.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T21:33:45-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User toEntity(UserCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setName( dto.getName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );
        user.setDocumentType( dto.getDocumentType() );
        user.setDocument( dto.getDocument() );

        return user;
    }

    @Override
    public void updateEntityFromDto(UserCreateDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        entity.setName( dto.getName() );
        entity.setLastName( dto.getLastName() );
        entity.setEmail( dto.getEmail() );
        entity.setPassword( dto.getPassword() );
        entity.setDocumentType( dto.getDocumentType() );
        entity.setDocument( dto.getDocument() );
    }

    @Override
    public UserResponseDTO toDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId( entity.getId() );
        userResponseDTO.setName( entity.getName() );
        userResponseDTO.setLastName( entity.getLastName() );
        userResponseDTO.setEmail( entity.getEmail() );
        userResponseDTO.setProfilePicture( entity.getProfilePicture() );
        userResponseDTO.setStatus( entity.getStatus() );
        userResponseDTO.setCreatedAt( entity.getCreatedAt() );
        userResponseDTO.setUpdatedAt( entity.getUpdatedAt() );
        userResponseDTO.setLastLogin( entity.getLastLogin() );
        userResponseDTO.setRoles( roleSetToRoleResponseDTOSet( entity.getRoles() ) );

        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> toDtoList(List<User> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<UserResponseDTO>( entities.size() );
        for ( User user : entities ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    protected Set<RoleResponseDTO> roleSetToRoleResponseDTOSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponseDTO> set1 = new LinkedHashSet<RoleResponseDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( roleMapper.toDto( role ) );
        }

        return set1;
    }
}
