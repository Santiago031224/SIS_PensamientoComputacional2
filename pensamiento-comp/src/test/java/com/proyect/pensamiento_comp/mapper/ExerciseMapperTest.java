package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ExerciseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ExerciseResponseDTO;
import com.proyect.pensamiento_comp.model.Exercise;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseMapperTest {
    private final ExerciseMapper mapper = Mappers.getMapper(ExerciseMapper.class);

    @Test
    void testToEntity_fullData() {

        ExerciseCreateDTO dto = new ExerciseCreateDTO();
        dto.setDescription("Ejercicio 1");
        dto.setDifficulty("Facil");


        Exercise entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId()); // id ignored
        assertEquals("Ejercicio 1", entity.getDescription());
        assertEquals("Facil", entity.getDifficulty());

    }

    @Test
    void testToDto_fullData() {

        Exercise entity = new Exercise();
        entity.setId(10L);
        entity.setDescription("Desc");
        entity.setDifficulty("Dif");


        ExerciseResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);

    }

    @Test
    void testToDtoList_multipleElements() {

        Exercise entity1 = new Exercise();
        entity1.setId(1L);
        entity1.setDescription("D1");
        entity1.setDifficulty("F");
        Exercise entity2 = new Exercise();
        entity2.setId(2L);
        entity2.setDescription("D2");
        entity2.setDifficulty("M");
        List<Exercise> entities = Arrays.asList(entity1, entity2);


        List<ExerciseResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void testToEntity_minimalDescription() {

        ExerciseCreateDTO dto = new ExerciseCreateDTO();
        dto.setDescription("Solo desc");


        Exercise entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Solo desc", entity.getDescription());
    }
}
