package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.GroupCreateDTO;
import com.proyect.pensamiento_comp.dto.response.GroupResponseDTO;
import com.proyect.pensamiento_comp.model.Group;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    Group toEntity(GroupCreateDTO dto);

    @Mapping(source = "period.id", target = "periodId")
    GroupResponseDTO toDto(Group entity);

    List<GroupResponseDTO> toDtoList(List<Group> entities);
}
