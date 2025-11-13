package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ActivityExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityExerciseResponseDTO;
import com.proyect.pensamiento_comp.model.ActivityExercise;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityExerciseMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "activity", ignore = true),
            @Mapping(target = "exercise", ignore = true)
    })
    ActivityExercise toEntity(ActivityExerciseCreateDTO dto);

    @Mappings({
            @Mapping(source = "activity.id", target = "activityId"),
            @Mapping(source = "exercise.id", target = "exerciseId")
    })
    ActivityExerciseResponseDTO toDto(ActivityExercise entity);

    List<ActivityExerciseResponseDTO> toDtoList(List<ActivityExercise> entities);
}
