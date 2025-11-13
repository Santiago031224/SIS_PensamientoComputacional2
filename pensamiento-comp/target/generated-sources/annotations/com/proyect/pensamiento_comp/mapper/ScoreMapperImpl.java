package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.SubmissionDocument;
import com.proyect.pensamiento_comp.dto.ScoreCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ScoreResponseDTO;
import com.proyect.pensamiento_comp.model.Score;
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
public class ScoreMapperImpl implements ScoreMapper {

    @Override
    public Score toEntity(ScoreCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Score score = new Score();

        score.setPointsAwarded( dto.getPointsAwarded() );

        return score;
    }

    @Override
    public ScoreResponseDTO toDto(Score entity) {
        if ( entity == null ) {
            return null;
        }

        ScoreResponseDTO scoreResponseDTO = new ScoreResponseDTO();

        scoreResponseDTO.setSubmissionId( entitySubmissionId( entity ) );
        scoreResponseDTO.setAssignmentDate( entity.getAssignmentDate() );
        scoreResponseDTO.setId( entity.getId() );
        scoreResponseDTO.setPointsAwarded( entity.getPointsAwarded() );

        return scoreResponseDTO;
    }

    @Override
    public List<ScoreResponseDTO> toDtoList(List<Score> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ScoreResponseDTO> list = new ArrayList<ScoreResponseDTO>( entities.size() );
        for ( Score score : entities ) {
            list.add( toDto( score ) );
        }

        return list;
    }

    @Override
    public SubmissionDocument.ExerciseGrade dtoToGrade(ScoreCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SubmissionDocument.ExerciseGrade exerciseGrade = new SubmissionDocument.ExerciseGrade();

        if ( dto.getPointsAwarded() != null ) {
            exerciseGrade.setPointsAwarded( dto.getPointsAwarded().intValue() );
        }

        return exerciseGrade;
    }

    @Override
    public ScoreResponseDTO gradeToDto(SubmissionDocument.ExerciseGrade grade) {
        if ( grade == null ) {
            return null;
        }

        ScoreResponseDTO scoreResponseDTO = new ScoreResponseDTO();

        if ( grade.getPointsAwarded() != null ) {
            scoreResponseDTO.setPointsAwarded( grade.getPointsAwarded().doubleValue() );
        }

        return scoreResponseDTO;
    }

    private Long entitySubmissionId(Score score) {
        if ( score == null ) {
            return null;
        }
        Submission submission = score.getSubmission();
        if ( submission == null ) {
            return null;
        }
        Long id = submission.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
