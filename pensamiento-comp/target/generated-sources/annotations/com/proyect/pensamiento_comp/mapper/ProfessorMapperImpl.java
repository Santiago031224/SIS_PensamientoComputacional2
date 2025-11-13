package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ProfessorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ProfessorResponseDTO;
import com.proyect.pensamiento_comp.model.Professor;
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
public class ProfessorMapperImpl implements ProfessorMapper {

    @Override
    public Professor toEntity(ProfessorCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Professor professor = new Professor();

        professor.setOfficeLocation( dto.getOfficeLocation() );
        professor.setSpecialtyArea( dto.getSpecialtyArea() );
        professor.setMaxGroupsAllowed( dto.getMaxGroupsAllowed() );
        professor.setAcademicRank( dto.getAcademicRank() );

        return professor;
    }

    @Override
    public ProfessorResponseDTO toDto(Professor entity) {
        if ( entity == null ) {
            return null;
        }

        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO();

        professorResponseDTO.setUserId( entityUserId( entity ) );
        professorResponseDTO.setId( entity.getId() );
        professorResponseDTO.setOfficeLocation( entity.getOfficeLocation() );
        professorResponseDTO.setSpecialtyArea( entity.getSpecialtyArea() );
        professorResponseDTO.setMaxGroupsAllowed( entity.getMaxGroupsAllowed() );
        professorResponseDTO.setAcademicRank( entity.getAcademicRank() );

        return professorResponseDTO;
    }

    @Override
    public List<ProfessorResponseDTO> toDtoList(List<Professor> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProfessorResponseDTO> list = new ArrayList<ProfessorResponseDTO>( entities.size() );
        for ( Professor professor : entities ) {
            list.add( toDto( professor ) );
        }

        return list;
    }

    private Long entityUserId(Professor professor) {
        if ( professor == null ) {
            return null;
        }
        User user = professor.getUser();
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
