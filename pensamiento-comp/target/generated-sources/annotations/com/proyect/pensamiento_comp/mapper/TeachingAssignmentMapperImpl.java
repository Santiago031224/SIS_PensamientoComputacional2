package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.TeachingAssignmentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import com.proyect.pensamiento_comp.model.Course;
import com.proyect.pensamiento_comp.model.Group;
import com.proyect.pensamiento_comp.model.Professor;
import com.proyect.pensamiento_comp.model.TeachingAssignment;
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
public class TeachingAssignmentMapperImpl implements TeachingAssignmentMapper {

    @Override
    public TeachingAssignment toEntity(TeachingAssignmentCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TeachingAssignment teachingAssignment = new TeachingAssignment();

        teachingAssignment.setAssignedDate( dto.getAssignedDate() );
        teachingAssignment.setStatus( dto.getStatus() );
        teachingAssignment.setEvaluationWeight( dto.getEvaluationWeight() );
        teachingAssignment.setRoleInCourse( dto.getRoleInCourse() );
        teachingAssignment.setClassroomLocation( dto.getClassroomLocation() );
        teachingAssignment.setNotes( dto.getNotes() );

        return teachingAssignment;
    }

    @Override
    public TeachingAssignmentResponseDTO toDto(TeachingAssignment entity) {
        if ( entity == null ) {
            return null;
        }

        TeachingAssignmentResponseDTO teachingAssignmentResponseDTO = new TeachingAssignmentResponseDTO();

        teachingAssignmentResponseDTO.setCourseId( entityCourseId( entity ) );
        teachingAssignmentResponseDTO.setProfessorId( entityProfessorId( entity ) );
        teachingAssignmentResponseDTO.setGroupId( entityGroupId( entity ) );
        teachingAssignmentResponseDTO.setId( entity.getId() );
        teachingAssignmentResponseDTO.setAssignedDate( entity.getAssignedDate() );
        teachingAssignmentResponseDTO.setStatus( entity.getStatus() );
        teachingAssignmentResponseDTO.setEvaluationWeight( entity.getEvaluationWeight() );
        teachingAssignmentResponseDTO.setRoleInCourse( entity.getRoleInCourse() );
        teachingAssignmentResponseDTO.setClassroomLocation( entity.getClassroomLocation() );
        teachingAssignmentResponseDTO.setNotes( entity.getNotes() );

        return teachingAssignmentResponseDTO;
    }

    @Override
    public List<TeachingAssignmentResponseDTO> toDtoList(List<TeachingAssignment> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TeachingAssignmentResponseDTO> list = new ArrayList<TeachingAssignmentResponseDTO>( entities.size() );
        for ( TeachingAssignment teachingAssignment : entities ) {
            list.add( toDto( teachingAssignment ) );
        }

        return list;
    }

    private Long entityCourseId(TeachingAssignment teachingAssignment) {
        if ( teachingAssignment == null ) {
            return null;
        }
        Course course = teachingAssignment.getCourse();
        if ( course == null ) {
            return null;
        }
        Long id = course.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityProfessorId(TeachingAssignment teachingAssignment) {
        if ( teachingAssignment == null ) {
            return null;
        }
        Professor professor = teachingAssignment.getProfessor();
        if ( professor == null ) {
            return null;
        }
        Long id = professor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityGroupId(TeachingAssignment teachingAssignment) {
        if ( teachingAssignment == null ) {
            return null;
        }
        Group group = teachingAssignment.getGroup();
        if ( group == null ) {
            return null;
        }
        Long id = group.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
