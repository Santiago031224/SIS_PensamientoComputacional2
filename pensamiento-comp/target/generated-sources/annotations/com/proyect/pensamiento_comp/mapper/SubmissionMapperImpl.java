package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import com.proyect.pensamiento_comp.dto.SubmissionCreateDTO;
import com.proyect.pensamiento_comp.dto.response.SubmissionResponseDTO;
import com.proyect.pensamiento_comp.model.Exercise;
import com.proyect.pensamiento_comp.model.Student;
import com.proyect.pensamiento_comp.model.Submission;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T00:08:44-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class SubmissionMapperImpl implements SubmissionMapper {

    @Override
    public Submission toEntity(SubmissionCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Submission submission = new Submission();

        submission.setLink( dto.getLink() );
        submission.setStatus( dto.getStatus() );

        return submission;
    }

    @Override
    public SubmissionResponseDTO toDto(Submission entity) {
        if ( entity == null ) {
            return null;
        }

        SubmissionResponseDTO submissionResponseDTO = new SubmissionResponseDTO();

        submissionResponseDTO.setStudentId( entityStudentId( entity ) );
        submissionResponseDTO.setExerciseId( entityExerciseId( entity ) );
        submissionResponseDTO.setCreatedAt( entity.getCreatedAt() );
        submissionResponseDTO.setDate( entity.getDate() );
        submissionResponseDTO.setId( entity.getId() );
        submissionResponseDTO.setLink( entity.getLink() );
        submissionResponseDTO.setStatus( entity.getStatus() );

        return submissionResponseDTO;
    }

    @Override
    public List<SubmissionResponseDTO> toDtoList(List<Submission> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SubmissionResponseDTO> list = new ArrayList<SubmissionResponseDTO>( entities.size() );
        for ( Submission submission : entities ) {
            list.add( toDto( submission ) );
        }

        return list;
    }

    @Override
    public SubmissionDocument dtoToDocument(SubmissionCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SubmissionDocument submissionDocument = new SubmissionDocument();

        submissionDocument.setStudentId( dto.getStudentId() );

        return submissionDocument;
    }

    @Override
    public SubmissionResponseDTO documentToDto(SubmissionDocument document) {
        if ( document == null ) {
            return null;
        }

        SubmissionResponseDTO submissionResponseDTO = new SubmissionResponseDTO();

        if ( document.getId() != null ) {
            submissionResponseDTO.setId( Long.parseLong( document.getId() ) );
        }
        submissionResponseDTO.setStudentId( document.getStudentId() );

        return submissionResponseDTO;
    }

    private Long entityStudentId(Submission submission) {
        if ( submission == null ) {
            return null;
        }
        Student student = submission.getStudent();
        if ( student == null ) {
            return null;
        }
        Long id = student.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityExerciseId(Submission submission) {
        if ( submission == null ) {
            return null;
        }
        Exercise exercise = submission.getExercise();
        if ( exercise == null ) {
            return null;
        }
        Long id = exercise.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
