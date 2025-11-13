package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.TeachingAssignmentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import com.proyect.pensamiento_comp.model.TeachingAssignment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeachingAssignmentMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "course", ignore = true),
            @Mapping(target = "professor", ignore = true),
            @Mapping(target = "group", ignore = true)
    })
    TeachingAssignment toEntity(TeachingAssignmentCreateDTO dto);

    @Mappings({
            @Mapping(source = "course.id", target = "courseId"),
            @Mapping(source = "professor.id", target = "professorId"),
            @Mapping(source = "group.id", target = "groupId")
    })
    TeachingAssignmentResponseDTO toDto(TeachingAssignment entity);

    List<TeachingAssignmentResponseDTO> toDtoList(List<TeachingAssignment> entities);
}
