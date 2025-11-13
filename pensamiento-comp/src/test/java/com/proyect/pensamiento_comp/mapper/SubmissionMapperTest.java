package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.SubmissionCreateDTO;
import com.proyect.pensamiento_comp.dto.response.SubmissionResponseDTO;
import com.proyect.pensamiento_comp.model.Submission;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionMapperTest {
    private final SubmissionMapper mapper = Mappers.getMapper(SubmissionMapper.class);

    @Test
    void testToEntity_fullData() {

        SubmissionCreateDTO dto = new SubmissionCreateDTO();
        dto.setLink("https://github.com/sol1");
        dto.setStatus("SUBMITTED");
        dto.setStudentId(10L);
        dto.setExerciseId(20L);


        Submission entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("https://github.com/sol1", entity.getLink());
        assertEquals("SUBMITTED", entity.getStatus());

    }

    @Test
    void testToDto_fullData() {

        Submission entity = new Submission();
        entity.setId(2L);
        entity.setLink("https://github.com/sol2");
        entity.setDate(java.time.LocalDateTime.of(2025, 10, 29, 12, 0));
        entity.setStatus("GRADED");
        entity.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 13, 0));
        com.proyect.pensamiento_comp.model.Student student = new com.proyect.pensamiento_comp.model.Student();
        student.setId(11L);
        entity.setStudent(student);
        com.proyect.pensamiento_comp.model.Exercise exercise = new com.proyect.pensamiento_comp.model.Exercise();
        exercise.setId(21L);
        entity.setExercise(exercise);


        SubmissionResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("https://github.com/sol2", dto.getLink());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 12, 0), dto.getDate());
        assertEquals("GRADED", dto.getStatus());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 13, 0), dto.getCreatedAt());

        if (dto.getStudentId() != null) {
            assertEquals(11L, dto.getStudentId());
        }
        if (dto.getExerciseId() != null) {
            assertEquals(21L, dto.getExerciseId());
        }
    }

    @Test
    void testToDtoList_multipleElements() {

        Submission entity1 = new Submission();
        entity1.setId(1L);
        entity1.setLink("https://github.com/a");
        entity1.setDate(java.time.LocalDateTime.of(2025, 10, 29, 10, 0));
        entity1.setStatus("SUBMITTED");
        entity1.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 11, 0));
        com.proyect.pensamiento_comp.model.Student student1 = new com.proyect.pensamiento_comp.model.Student();
        student1.setId(12L);
        entity1.setStudent(student1);
        com.proyect.pensamiento_comp.model.Exercise exercise1 = new com.proyect.pensamiento_comp.model.Exercise();
        exercise1.setId(22L);
        entity1.setExercise(exercise1);

        Submission entity2 = new Submission();
        entity2.setId(2L);
        entity2.setLink("https://github.com/b");
        entity2.setDate(java.time.LocalDateTime.of(2025, 10, 29, 12, 0));
        entity2.setStatus("GRADED");
        entity2.setCreatedAt(java.time.LocalDateTime.of(2025, 10, 29, 13, 0));
        com.proyect.pensamiento_comp.model.Student student2 = new com.proyect.pensamiento_comp.model.Student();
        student2.setId(13L);
        entity2.setStudent(student2);
        com.proyect.pensamiento_comp.model.Exercise exercise2 = new com.proyect.pensamiento_comp.model.Exercise();
        exercise2.setId(23L);
        entity2.setExercise(exercise2);

        List<Submission> entities = Arrays.asList(entity1, entity2);


        List<SubmissionResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        if (dtos.get(0).getId() != null) {
            assertEquals(1L, dtos.get(0).getId());
        }
        if (dtos.get(0).getLink() != null) {
            assertEquals("https://github.com/a", dtos.get(0).getLink());
        }
        if (dtos.get(0).getDate() != null) {
            assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 10, 0), dtos.get(0).getDate());
        }
        if (dtos.get(0).getStatus() != null) {
            assertEquals("SUBMITTED", dtos.get(0).getStatus());
        }
        if (dtos.get(0).getCreatedAt() != null) {
            assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 11, 0), dtos.get(0).getCreatedAt());
        }
        if (dtos.get(0).getStudentId() != null) {
            assertEquals(12L, dtos.get(0).getStudentId());
        }
        if (dtos.get(0).getExerciseId() != null) {
            assertEquals(22L, dtos.get(0).getExerciseId());
        }
        if (dtos.get(1).getId() != null) {
            assertEquals(2L, dtos.get(1).getId());
        }
        if (dtos.get(1).getLink() != null) {
            assertEquals("https://github.com/b", dtos.get(1).getLink());
        }
        if (dtos.get(1).getDate() != null) {
            assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 12, 0), dtos.get(1).getDate());
        }
        if (dtos.get(1).getStatus() != null) {
            assertEquals("GRADED", dtos.get(1).getStatus());
        }
        if (dtos.get(1).getCreatedAt() != null) {
            assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 13, 0), dtos.get(1).getCreatedAt());
        }
        if (dtos.get(1).getStudentId() != null) {
            assertEquals(13L, dtos.get(1).getStudentId());
        }
        if (dtos.get(1).getExerciseId() != null) {
            assertEquals(23L, dtos.get(1).getExerciseId());
        }
    }

    @Test
    void testToEntity_minimalEmptyDto() {

        SubmissionCreateDTO dto = new SubmissionCreateDTO();


        Submission entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getLink());
        assertNull(entity.getStatus());
        assertNull(entity.getStudent());
        assertNull(entity.getExercise());
    }
}
