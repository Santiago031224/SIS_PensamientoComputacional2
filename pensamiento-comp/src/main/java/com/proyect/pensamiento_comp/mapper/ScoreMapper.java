package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import com.proyect.pensamiento_comp.dto.ScoreCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ScoreResponseDTO;
import com.proyect.pensamiento_comp.model.Score;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScoreMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "submission", ignore = true),
            @Mapping(target = "assignmentDate", ignore = true)
    })
    Score toEntity(ScoreCreateDTO dto);

    @Mapping(source = "submission.id", target = "submissionId")
    ScoreResponseDTO toDto(Score entity);

    List<ScoreResponseDTO> toDtoList(List<Score> entities);
    
    // MongoDB mappings - Score is embedded in SubmissionDocument.grading.breakdown
    @Mapping(target = "exerciseId", ignore = true) 
    @Mapping(source = "pointsAwarded", target = "pointsAwarded")
    SubmissionDocument.ExerciseGrade dtoToGrade(ScoreCreateDTO dto);
    
    @Mapping(source = "pointsAwarded", target = "pointsAwarded")
    @Mapping(target = "submissionId", ignore = true)
    ScoreResponseDTO gradeToDto(SubmissionDocument.ExerciseGrade grade);
}
