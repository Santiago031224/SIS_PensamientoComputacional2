package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ProfessorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ProfessorResponseDTO;
import com.proyect.pensamiento_comp.model.Professor;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessorMapper {

    @Mapping(target = "id", ignore = true)
    Professor toEntity(ProfessorCreateDTO dto);

    @Mapping(source = "user.id", target = "userId")
    ProfessorResponseDTO toDto(Professor entity);

    List<ProfessorResponseDTO> toDtoList(List<Professor> entities);
}
