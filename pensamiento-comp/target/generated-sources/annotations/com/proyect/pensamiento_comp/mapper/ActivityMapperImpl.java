package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.dto.ActivityCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityResponseDTO;
import com.proyect.pensamiento_comp.model.Activity;
import com.proyect.pensamiento_comp.model.Exercise;
import com.proyect.pensamiento_comp.model.Professor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T21:33:45-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ActivityMapperImpl implements ActivityMapper {

    @Override
    public Activity toEntity(ActivityCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Activity activity = new Activity();

        activity.setTitle( dto.getTitle() );
        activity.setDescription( dto.getDescription() );
        activity.setStartDate( dto.getStartDate() );
        activity.setEndDate( dto.getEndDate() );

        return activity;
    }

    @Override
    public ActivityResponseDTO toDto(Activity entity) {
        if ( entity == null ) {
            return null;
        }

        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO();

        activityResponseDTO.setProfessorId( entityProfessorId( entity ) );
        activityResponseDTO.setId( entity.getId() );
        activityResponseDTO.setTitle( entity.getTitle() );
        activityResponseDTO.setStartDate( entity.getStartDate() );
        activityResponseDTO.setEndDate( entity.getEndDate() );
        activityResponseDTO.setDescription( entity.getDescription() );
        activityResponseDTO.setExercises( exerciseSetToExerciseEmbedDTOList( entity.getExercises() ) );

        return activityResponseDTO;
    }

    @Override
    public List<ActivityResponseDTO> toDtoList(List<Activity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ActivityResponseDTO> list = new ArrayList<ActivityResponseDTO>( entities.size() );
        for ( Activity activity : entities ) {
            list.add( toDto( activity ) );
        }

        return list;
    }

    @Override
    public ActivityDocument dtoToDocument(ActivityCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ActivityDocument activityDocument = new ActivityDocument();

        activityDocument.setTitle( dto.getTitle() );
        activityDocument.setDescription( dto.getDescription() );
        activityDocument.setProfessorId( dto.getProfessorId() );

        setSchedule( activityDocument, dto );

        return activityDocument;
    }

    @Override
    public ActivityResponseDTO documentToDto(ActivityDocument document) {
        if ( document == null ) {
            return null;
        }

        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO();

        activityResponseDTO.setId( document.getRelationalId() );
        activityResponseDTO.setTitle( document.getTitle() );
        activityResponseDTO.setDescription( document.getDescription() );
        activityResponseDTO.setStartDate( document.getStartDate() );
        activityResponseDTO.setEndDate( document.getEndDate() );
        activityResponseDTO.setProfessorId( document.getProfessorId() );
        activityResponseDTO.setProfessorName( document.getProfessorName() );
        activityResponseDTO.setExercises( exerciseEmbedListToExerciseEmbedDTOList( document.getExercises() ) );

        return activityResponseDTO;
    }

    private Long entityProfessorId(Activity activity) {
        if ( activity == null ) {
            return null;
        }
        Professor professor = activity.getProfessor();
        if ( professor == null ) {
            return null;
        }
        Long id = professor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected ActivityResponseDTO.ExerciseEmbedDTO exerciseToExerciseEmbedDTO(Exercise exercise) {
        if ( exercise == null ) {
            return null;
        }

        ActivityResponseDTO.ExerciseEmbedDTO exerciseEmbedDTO = new ActivityResponseDTO.ExerciseEmbedDTO();

        exerciseEmbedDTO.setTitle( exercise.getTitle() );
        exerciseEmbedDTO.setDescription( exercise.getDescription() );
        exerciseEmbedDTO.setDifficulty( exercise.getDifficulty() );

        return exerciseEmbedDTO;
    }

    protected List<ActivityResponseDTO.ExerciseEmbedDTO> exerciseSetToExerciseEmbedDTOList(Set<Exercise> set) {
        if ( set == null ) {
            return null;
        }

        List<ActivityResponseDTO.ExerciseEmbedDTO> list = new ArrayList<ActivityResponseDTO.ExerciseEmbedDTO>( set.size() );
        for ( Exercise exercise : set ) {
            list.add( exerciseToExerciseEmbedDTO( exercise ) );
        }

        return list;
    }

    protected List<ActivityResponseDTO.ExerciseEmbedDTO> exerciseEmbedListToExerciseEmbedDTOList(List<ActivityDocument.ExerciseEmbed> list) {
        if ( list == null ) {
            return null;
        }

        List<ActivityResponseDTO.ExerciseEmbedDTO> list1 = new ArrayList<ActivityResponseDTO.ExerciseEmbedDTO>( list.size() );
        for ( ActivityDocument.ExerciseEmbed exerciseEmbed : list ) {
            list1.add( exerciseEmbedToDto( exerciseEmbed ) );
        }

        return list1;
    }
}
