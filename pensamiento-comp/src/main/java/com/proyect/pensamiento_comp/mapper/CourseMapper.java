package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.CourseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.CourseResponseDTO;
import com.proyect.pensamiento_comp.model.Course;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseCreateDTO dto);

    @Mapping(source = "administrator.id", target = "administratorId")
    CourseResponseDTO toDto(Course entity);

    List<CourseResponseDTO> toDtoList(List<Course> entities);
}
