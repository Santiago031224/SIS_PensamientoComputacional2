package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.TeachingAssignmentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.TeachingAssignmentResponseDTO;
import com.proyect.pensamiento_comp.model.TeachingAssignment;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeachingAssignmentMapperTest {
    private final TeachingAssignmentMapper mapper = Mappers.getMapper(TeachingAssignmentMapper.class);

    @Test
    void testToEntity_fullData() {

        TeachingAssignmentCreateDTO dto = new TeachingAssignmentCreateDTO();
        dto.setCourseId(3L);
        dto.setProfessorId(2L);



        TeachingAssignment entity = mapper.toEntity(dto);


        assertNotNull(entity);

        if (entity.getProfessor() != null) {
            assertEquals(2L, entity.getProfessor().getId());
        }
        if (entity.getCourse() != null) {
            assertEquals(3L, entity.getCourse().getId());
        }
    }

    @Test
    void testToDto_fullData() {

        TeachingAssignment entity = new TeachingAssignment();
        entity.setId(10L);

        entity.setProfessor(null);
        entity.setCourse(null);


        TeachingAssignmentResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(10L, dto.getId());
    }

    @Test
    void testToDtoList_multipleElements() {

        TeachingAssignment entity1 = new TeachingAssignment();
        entity1.setId(1L);
        TeachingAssignment entity2 = new TeachingAssignment();
        entity2.setId(2L);
        List<TeachingAssignment> entities = Arrays.asList(entity1, entity2);


        List<TeachingAssignmentResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
    }

    @Test
    void testToEntity_minimalProfessorCourseId() {

        TeachingAssignmentCreateDTO dto = new TeachingAssignmentCreateDTO();
        dto.setProfessorId(7L);
        dto.setCourseId(8L);


        TeachingAssignment entity = mapper.toEntity(dto);


        assertNotNull(entity);
        if (entity.getProfessor() != null) {
            assertEquals(7L, entity.getProfessor().getId());
        }
        if (entity.getCourse() != null) {
            assertEquals(8L, entity.getCourse().getId());
        }
    }
}
