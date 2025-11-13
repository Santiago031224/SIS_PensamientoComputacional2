package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.PointCodeDocument;
import com.proyect.pensamiento_comp.dto.PointCodeCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PointCodeResponseDTO;
import com.proyect.pensamiento_comp.model.PointCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PointCodeMapper {

    @Mapping(target = "activityId", source = "activity.id")
    @Mapping(target = "studentId", source = "student.id")
    PointCodeResponseDTO toDto(PointCode entity);

    List<PointCodeResponseDTO> toDtoList(List<PointCode> entities);

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "student", ignore = true)
    PointCode toEntity(PointCodeCreateDTO dto);
    
    // MongoDB mappings
    @Mapping(target = "id", ignore = true)
    PointCodeDocument dtoToDocument(PointCodeCreateDTO dto);
    
    PointCodeResponseDTO documentToDto(PointCodeDocument document);
}
