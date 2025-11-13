package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PeriodCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PeriodResponseDTO;
import com.proyect.pensamiento_comp.model.Period;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PeriodMapper {

    @Mapping(target = "id", ignore = true)
    Period toEntity(PeriodCreateDTO dto);

    PeriodResponseDTO toDto(Period entity);

    List<PeriodResponseDTO> toDtoList(List<Period> entities);
}
