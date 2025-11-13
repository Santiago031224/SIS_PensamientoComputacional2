package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import com.proyect.pensamiento_comp.dto.SubmissionCreateDTO;
import com.proyect.pensamiento_comp.dto.response.SubmissionResponseDTO;
import com.proyect.pensamiento_comp.model.Submission;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubmissionMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "student", ignore = true), 
            @Mapping(target = "exercise", ignore = true),
            @Mapping(target = "scores", ignore = true),
            @Mapping(target = "files", ignore = true)
    })
    Submission toEntity(SubmissionCreateDTO dto);

    @Mappings({
            @Mapping(source = "student.id", target = "studentId"),
            @Mapping(source = "exercise.id", target = "exerciseId")
    })
    SubmissionResponseDTO toDto(Submission entity);

    List<SubmissionResponseDTO> toDtoList(List<Submission> entities);
    
    // MongoDB mappings - manual mapping needed for complex nested structures
    SubmissionDocument dtoToDocument(SubmissionCreateDTO dto);
    SubmissionResponseDTO documentToDto(SubmissionDocument document);
}
