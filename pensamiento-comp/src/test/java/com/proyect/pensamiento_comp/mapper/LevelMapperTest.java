package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.LevelCreateDTO;
import com.proyect.pensamiento_comp.dto.response.LevelResponseDTO;
import com.proyect.pensamiento_comp.model.Level;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelMapperTest {
    private final LevelMapper mapper = Mappers.getMapper(LevelMapper.class);

    @Test
    void testToEntity_fullData() {

        LevelCreateDTO dto = new LevelCreateDTO();
        dto.setName("Nivel 1");
        dto.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 12, 0));


        Level entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Nivel 1", entity.getName());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 12, 0), entity.getCreatedAt());
    }

    @Test
    void testToDto_fullData() {

        Level entity = new Level();
        entity.setId(2L);
        entity.setName("N2");
        entity.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 13, 0));


        LevelResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("N2", dto.getName());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 13, 0), dto.getCreatedAt());
    }

    @Test
    void testToDtoList_multipleElements() {

        Level entity1 = new Level();
        entity1.setId(1L);
        entity1.setName("N1");
        entity1.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 14, 0));
        Level entity2 = new Level();
        entity2.setId(2L);
        entity2.setName("N2");
        entity2.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 15, 0));
        List<Level> entities = Arrays.asList(entity1, entity2);


        List<LevelResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("N1", dtos.get(0).getName());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 14, 0), dtos.get(0).getCreatedAt());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("N2", dtos.get(1).getName());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 15, 0), dtos.get(1).getCreatedAt());
    }

    @Test
    void testToEntity_minimalName() {

        LevelCreateDTO dto = new LevelCreateDTO();
        dto.setName("MinLevel");


        Level entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("MinLevel", entity.getName());
        assertNull(entity.getId());
    }
}
