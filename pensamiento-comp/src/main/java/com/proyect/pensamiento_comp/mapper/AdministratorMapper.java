package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.AdministratorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.AdministratorResponseDTO;
import com.proyect.pensamiento_comp.model.Administrator;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdministratorMapper {

    @Mapping(target = "id", ignore = true)
    Administrator toEntity(AdministratorCreateDTO dto);

    @Mapping(source = "user.id", target = "userId")
    AdministratorResponseDTO toDto(Administrator entity);

    List<AdministratorResponseDTO> toDtoList(List<Administrator> entities);
}
