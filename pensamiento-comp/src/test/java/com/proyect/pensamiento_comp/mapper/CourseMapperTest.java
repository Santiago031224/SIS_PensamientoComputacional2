package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.CourseCreateDTO;
import com.proyect.pensamiento_comp.dto.response.CourseResponseDTO;
import com.proyect.pensamiento_comp.model.Course;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseMapperTest {
    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);

    @Test
    void testToEntity_fullData() {

        CourseCreateDTO dto = new CourseCreateDTO();
        dto.setName("Curso 1");
        dto.setAdministratorId(5L);


        Course entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Curso 1", entity.getName());
        if (entity.getAdministrator() != null) {
            assertEquals(5L, entity.getAdministrator().getId());
        }
    }

    @Test
    void testToDto_fullData() {

        Course entity = new Course();
        entity.setId(2L);
        entity.setName("C2");



        CourseResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("C2", dto.getName());
    }

    @Test
    void testToDtoList_multipleElements() {

        Course entity1 = new Course();
        entity1.setId(1L);
        entity1.setName("C1");
        Course entity2 = new Course();
        entity2.setId(2L);
        entity2.setName("C2");
        List<Course> entities = Arrays.asList(entity1, entity2);


        List<CourseResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("C1", dtos.get(0).getName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("C2", dtos.get(1).getName());
    }

    @Test
    void testToEntity_minimalName() {

        CourseCreateDTO dto = new CourseCreateDTO();
        dto.setName("Solo nombre");


        Course entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Solo nombre", entity.getName());
    }
}
