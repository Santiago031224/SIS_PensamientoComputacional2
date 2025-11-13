package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ActivityExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityExerciseResponseDTO;
import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.ActivityExercise;
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
public class ActivityExerciseMapperImpl implements ActivityExerciseMapper {

    @Override
    public ActivityExercise toEntity(ActivityExerciseCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ActivityExercise activityExercise = new ActivityExercise();

        activityExercise.setPosition( dto.getPosition() );

        return activityExercise;
    }

    @Override
    public ActivityExerciseResponseDTO toDto(ActivityExercise entity) {
        if ( entity == null ) {
            return null;
        }

        ActivityExerciseResponseDTO activityExerciseResponseDTO = new ActivityExerciseResponseDTO();

        activityExerciseResponseDTO.setActivityId( entityActivityId( entity ) );
        activityExerciseResponseDTO.setExerciseId( entityExerciseId( entity ) );
        activityExerciseResponseDTO.setPosition( entity.getPosition() );

        return activityExerciseResponseDTO;
    }

    @Override
    public List<ActivityExerciseResponseDTO> toDtoList(List<ActivityExercise> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ActivityExerciseResponseDTO> list = new ArrayList<ActivityExerciseResponseDTO>( entities.size() );
        for ( ActivityExercise activityExercise : entities ) {
            list.add( toDto( activityExercise ) );
        }

        return list;
    }

    private Long entityActivityId(ActivityExercise activityExercise) {
        if ( activityExercise == null ) {
            return null;
        }
        Activity activity = activityExercise.getActivity();
        if ( activity == null ) {
            return null;
        }
        Long id = activity.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityExerciseId(ActivityExercise activityExercise) {
        if ( activityExercise == null ) {
            return null;
        }
        Exercise exercise = activityExercise.getExercise();
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
