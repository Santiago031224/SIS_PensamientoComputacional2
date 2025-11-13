package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ActivityCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ActivityResponseDTO;
import com.proyect.pensamiento_comp.model.Activity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityMapperTest {
    private final ActivityMapper mapper = Mappers.getMapper(ActivityMapper.class);

    @Test
    void testToEntity_fullData() {

        ActivityCreateDTO dto = new ActivityCreateDTO();
        dto.setTitle("Actividad 1");
        dto.setStartDate(LocalDate.of(2023, 1, 1));
        dto.setEndDate(LocalDate.of(2023, 1, 10));
        dto.setDescription("Desc");
        dto.setProfessorId(5L);


        Activity entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Actividad 1", entity.getTitle());
        assertEquals(LocalDate.of(2023, 1, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2023, 1, 10), entity.getEndDate());
        assertEquals("Desc", entity.getDescription());
        if (entity.getProfessor() != null) {
            assertEquals(5L, entity.getProfessor().getId());
        }
    }

    @Test
    void testToDto_fullData() {

        Activity entity = new Activity();
        entity.setId(10L);
        entity.setTitle("Act");
        entity.setStartDate(LocalDate.of(2024, 2, 2));
        entity.setEndDate(LocalDate.of(2024, 2, 12));
        entity.setDescription("Desc2");

        entity.setProfessor(null);


        ActivityResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Act", dto.getTitle());
        assertEquals(LocalDate.of(2024, 2, 2), dto.getStartDate());
        assertEquals(LocalDate.of(2024, 2, 12), dto.getEndDate());
        assertEquals("Desc2", dto.getDescription());
    }

    @Test
    void testToDtoList_multipleElements() {

        Activity entity1 = new Activity();
        entity1.setId(1L);
        entity1.setTitle("A1");
        entity1.setStartDate(LocalDate.of(2023, 1, 1));
        entity1.setEndDate(LocalDate.of(2023, 1, 2));
        entity1.setDescription("D1");
        Activity entity2 = new Activity();
        entity2.setId(2L);
        entity2.setTitle("A2");
        entity2.setStartDate(LocalDate.of(2023, 2, 1));
        entity2.setEndDate(LocalDate.of(2023, 2, 2));
        entity2.setDescription("D2");
        List<Activity> entities = Arrays.asList(entity1, entity2);


        List<ActivityResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("A1", dtos.get(0).getTitle());
        assertEquals("D1", dtos.get(0).getDescription());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("A2", dtos.get(1).getTitle());
        assertEquals("D2", dtos.get(1).getDescription());
    }

    @Test
    void testToEntity_minimalProfessorId() {

        ActivityCreateDTO dto = new ActivityCreateDTO();
        dto.setProfessorId(7L);


        Activity entity = mapper.toEntity(dto);


        assertNotNull(entity);
        if (entity.getProfessor() != null) {
            assertEquals(7L, entity.getProfessor().getId());
        }
    }
}

