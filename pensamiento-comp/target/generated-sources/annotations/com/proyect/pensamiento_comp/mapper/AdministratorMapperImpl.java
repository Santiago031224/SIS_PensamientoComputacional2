package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.AdministratorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.AdministratorResponseDTO;
import com.proyect.pensamiento_comp.model.Administrator;
import com.proyect.pensamiento_comp.model.User;
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
public class AdministratorMapperImpl implements AdministratorMapper {

    @Override
    public Administrator toEntity(AdministratorCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Administrator administrator = new Administrator();

        administrator.setDepartment( dto.getDepartment() );

        return administrator;
    }

    @Override
    public AdministratorResponseDTO toDto(Administrator entity) {
        if ( entity == null ) {
            return null;
        }

        AdministratorResponseDTO administratorResponseDTO = new AdministratorResponseDTO();

        administratorResponseDTO.setUserId( entityUserId( entity ) );
        administratorResponseDTO.setId( entity.getId() );
        administratorResponseDTO.setDepartment( entity.getDepartment() );

        return administratorResponseDTO;
    }

    @Override
    public List<AdministratorResponseDTO> toDtoList(List<Administrator> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AdministratorResponseDTO> list = new ArrayList<AdministratorResponseDTO>( entities.size() );
        for ( Administrator administrator : entities ) {
            list.add( toDto( administrator ) );
        }

        return list;
    }

    private Long entityUserId(Administrator administrator) {
        if ( administrator == null ) {
            return null;
        }
        User user = administrator.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
