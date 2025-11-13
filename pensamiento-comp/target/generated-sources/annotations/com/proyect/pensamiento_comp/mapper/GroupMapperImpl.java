package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.GroupCreateDTO;
import com.proyect.pensamiento_comp.dto.response.GroupResponseDTO;
import com.proyect.pensamiento_comp.model.Group;
import com.proyect.pensamiento_comp.model.Period;
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
public class GroupMapperImpl implements GroupMapper {

    @Override
    public Group toEntity(GroupCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Group group = new Group();

        group.setName( dto.getName() );

        return group;
    }

    @Override
    public GroupResponseDTO toDto(Group entity) {
        if ( entity == null ) {
            return null;
        }

        GroupResponseDTO groupResponseDTO = new GroupResponseDTO();

        groupResponseDTO.setPeriodId( entityPeriodId( entity ) );
        groupResponseDTO.setId( entity.getId() );
        groupResponseDTO.setName( entity.getName() );

        return groupResponseDTO;
    }

    @Override
    public List<GroupResponseDTO> toDtoList(List<Group> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GroupResponseDTO> list = new ArrayList<GroupResponseDTO>( entities.size() );
        for ( Group group : entities ) {
            list.add( toDto( group ) );
        }

        return list;
    }

    private Long entityPeriodId(Group group) {
        if ( group == null ) {
            return null;
        }
        Period period = group.getPeriod();
        if ( period == null ) {
            return null;
        }
        Long id = period.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
