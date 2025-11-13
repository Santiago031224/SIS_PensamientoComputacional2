package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.StudentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.StudentResponseDTO;
import com.proyect.pensamiento_comp.model.Student;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    Student toEntity(StudentCreateDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "level.id", target = "levelId")
    StudentResponseDTO toDto(Student entity);

    List<StudentResponseDTO> toDtoList(List<Student> entities);
}
