package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ExerciseResponseDTO;
import com.proyect.pensamiento_comp.model.Exercise;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {

    @Mapping(target = "id", ignore = true)
    Exercise toEntity(ExerciseCreateDTO dto);

    ExerciseResponseDTO toDto(Exercise entity);

    List<ExerciseResponseDTO> toDtoList(List<Exercise> entities);
}
