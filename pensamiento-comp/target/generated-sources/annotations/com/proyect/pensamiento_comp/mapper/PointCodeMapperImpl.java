package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.PointCodeDocument;
import com.proyect.pensamiento_comp.dto.PointCodeCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PointCodeResponseDTO;
import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.PointCode;
import com.proyect.pensamiento_comp.model.Student;
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
public class PointCodeMapperImpl implements PointCodeMapper {

    @Override
    public PointCodeResponseDTO toDto(PointCode entity) {
        if ( entity == null ) {
            return null;
        }

        PointCodeResponseDTO pointCodeResponseDTO = new PointCodeResponseDTO();

        pointCodeResponseDTO.setActivityId( entityActivityId( entity ) );
        pointCodeResponseDTO.setStudentId( entityStudentId( entity ) );
        pointCodeResponseDTO.setCode( entity.getCode() );
        pointCodeResponseDTO.setPoints( entity.getPoints() );
        pointCodeResponseDTO.setRedeemedAt( entity.getRedeemedAt() );
        pointCodeResponseDTO.setStatus( entity.getStatus() );
        pointCodeResponseDTO.setUsageLimit( entity.getUsageLimit() );

        return pointCodeResponseDTO;
    }

    @Override
    public List<PointCodeResponseDTO> toDtoList(List<PointCode> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PointCodeResponseDTO> list = new ArrayList<PointCodeResponseDTO>( entities.size() );
        for ( PointCode pointCode : entities ) {
            list.add( toDto( pointCode ) );
        }

        return list;
    }

    @Override
    public PointCode toEntity(PointCodeCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PointCode pointCode = new PointCode();

        pointCode.setCode( dto.getCode() );
        pointCode.setPoints( dto.getPoints() );
        pointCode.setRedeemedAt( dto.getRedeemedAt() );
        pointCode.setStatus( dto.getStatus() );
        pointCode.setUsageLimit( dto.getUsageLimit() );

        return pointCode;
    }

    @Override
    public PointCodeDocument dtoToDocument(PointCodeCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PointCodeDocument pointCodeDocument = new PointCodeDocument();

        if ( dto.getActivityId() != null ) {
            pointCodeDocument.setActivityId( String.valueOf( dto.getActivityId() ) );
        }
        pointCodeDocument.setPoints( dto.getPoints() );
        pointCodeDocument.setUsageLimit( dto.getUsageLimit() );
        pointCodeDocument.setStatus( dto.getStatus() );

        return pointCodeDocument;
    }

    @Override
    public PointCodeResponseDTO documentToDto(PointCodeDocument document) {
        if ( document == null ) {
            return null;
        }

        PointCodeResponseDTO pointCodeResponseDTO = new PointCodeResponseDTO();

        pointCodeResponseDTO.setPoints( document.getPoints() );
        pointCodeResponseDTO.setStatus( document.getStatus() );
        pointCodeResponseDTO.setUsageLimit( document.getUsageLimit() );
        if ( document.getActivityId() != null ) {
            pointCodeResponseDTO.setActivityId( Long.parseLong( document.getActivityId() ) );
        }

        return pointCodeResponseDTO;
    }

    private Long entityActivityId(PointCode pointCode) {
        if ( pointCode == null ) {
            return null;
        }
        Activity activity = pointCode.getActivity();
        if ( activity == null ) {
            return null;
        }
        Long id = activity.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityStudentId(PointCode pointCode) {
        if ( pointCode == null ) {
            return null;
        }
        Student student = pointCode.getStudent();
        if ( student == null ) {
            return null;
        }
        Long id = student.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
