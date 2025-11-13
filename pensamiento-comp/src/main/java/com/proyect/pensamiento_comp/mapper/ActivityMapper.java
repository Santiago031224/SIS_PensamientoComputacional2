package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.dto.ActivityCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityResponseDTO;
import com.proyect.pensamiento_comp.model.Activity;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    @Mapping(target = "id", ignore = true)
    Activity toEntity(ActivityCreateDTO dto);

    @Mapping(source = "professor.id", target = "professorId")
    ActivityResponseDTO toDto(Activity entity);

    List<ActivityResponseDTO> toDtoList(List<Activity> entities);
    
    // MongoDB mappings
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "professorId", target = "professorId")
    @Mapping(target = "schedule", ignore = true)
    ActivityDocument dtoToDocument(ActivityCreateDTO dto);
    
    @AfterMapping
    default void setSchedule(@MappingTarget ActivityDocument document, ActivityCreateDTO dto) {
        if (dto.getStartDate() != null || dto.getEndDate() != null) {
            ActivityDocument.Schedule schedule = new ActivityDocument.Schedule();
            if (dto.getStartDate() != null) {
                schedule.setStartDate(LocalDateTime.of(dto.getStartDate(), LocalTime.MIDNIGHT));
            }
            if (dto.getEndDate() != null) {
                schedule.setEndDate(LocalDateTime.of(dto.getEndDate(), LocalTime.of(23, 59, 59)));
            }
            document.setSchedule(schedule);
        }
    }
    
    @Mapping(source = "relationalId", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "professorId", target = "professorId")
    @Mapping(source = "professorName", target = "professorName")
    @Mapping(source = "exercises", target = "exercises")
    ActivityResponseDTO documentToDto(ActivityDocument document);
    
    // Mapper para ejercicios embebidos
    default ActivityResponseDTO.ExerciseEmbedDTO exerciseEmbedToDto(ActivityDocument.ExerciseEmbed embed) {
        if (embed == null) return null;
        ActivityResponseDTO.ExerciseEmbedDTO dto = new ActivityResponseDTO.ExerciseEmbedDTO();
        dto.setExerciseId(embed.getExerciseId());
        dto.setTitle(embed.getTitle());
        dto.setDescription(embed.getDescription());
        dto.setDifficulty(embed.getDifficulty());
        dto.setPosition(embed.getPosition());
        dto.setPointValue(embed.getPointValue());
        return dto;
    }
}
