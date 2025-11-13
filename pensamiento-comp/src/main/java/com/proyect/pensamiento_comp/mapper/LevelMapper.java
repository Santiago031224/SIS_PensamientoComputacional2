package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.LevelCreateDTO;
import com.proyect.pensamiento_comp.dto.response.LevelResponseDTO;
import com.proyect.pensamiento_comp.model.Level;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelMapper {

    @Mapping(target = "id", ignore = true)
    Level toEntity(LevelCreateDTO dto);

    LevelResponseDTO toDto(Level entity);

    List<LevelResponseDTO> toDtoList(List<Level> entities);
}
