package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ExerciseResponseDTO;
import com.proyect.pensamiento_comp.model.Exercise;
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
public class ExerciseMapperImpl implements ExerciseMapper {

    @Override
    public Exercise toEntity(ExerciseCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Exercise exercise = new Exercise();

        exercise.setTitle( dto.getTitle() );
        exercise.setDescription( dto.getDescription() );
        exercise.setDifficulty( dto.getDifficulty() );
        exercise.setProgramming_language( dto.getProgramming_language() );

        return exercise;
    }

    @Override
    public ExerciseResponseDTO toDto(Exercise entity) {
        if ( entity == null ) {
            return null;
        }

        ExerciseResponseDTO exerciseResponseDTO = new ExerciseResponseDTO();

        exerciseResponseDTO.setTitle( entity.getTitle() );
        exerciseResponseDTO.setProgramming_language( entity.getProgramming_language() );
        exerciseResponseDTO.setDescription( entity.getDescription() );
        exerciseResponseDTO.setDifficulty( entity.getDifficulty() );

        return exerciseResponseDTO;
    }

    @Override
    public List<ExerciseResponseDTO> toDtoList(List<Exercise> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ExerciseResponseDTO> list = new ArrayList<ExerciseResponseDTO>( entities.size() );
        for ( Exercise exercise : entities ) {
            list.add( toDto( exercise ) );
        }

        return list;
    }
}
