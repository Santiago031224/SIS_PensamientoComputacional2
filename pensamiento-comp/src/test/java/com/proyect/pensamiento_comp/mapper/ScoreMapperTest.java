package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ScoreCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ScoreResponseDTO;
import com.proyect.pensamiento_comp.model.Score;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreMapperTest {
    private final ScoreMapper mapper = Mappers.getMapper(ScoreMapper.class);

    @Test
    void testToEntity_fullData() {

        ScoreCreateDTO dto = new ScoreCreateDTO();
        dto.setPointsAwarded(100.0);
        dto.setSubmissionId(5L);


        Score entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(100.0, entity.getPointsAwarded());
        assertNull(entity.getSubmission());
        assertNull(entity.getAssignmentDate());
    }

    @Test
    void testToDto_fullData() {

        Score entity = new Score();
        entity.setId(2L);
        entity.setPointsAwarded(200.0);
        entity.setAssignmentDate(java.time.LocalDateTime.of(2023, 10, 29, 12, 0));
        com.proyect.pensamiento_comp.model.Submission submission = new com.proyect.pensamiento_comp.model.Submission();
        submission.setId(7L);
        entity.setSubmission(submission);


        ScoreResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(200.0, dto.getPointsAwarded());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 12, 0), dto.getAssignmentDate());
        assertEquals(7L, dto.getSubmissionId());
    }

    @Test
    void testToDtoList_multipleElements() {

        Score entity1 = new Score();
        entity1.setId(1L);
        entity1.setPointsAwarded(10.0);
        entity1.setAssignmentDate(java.time.LocalDateTime.of(2023, 10, 29, 10, 0));
        com.proyect.pensamiento_comp.model.Submission submission1 = new com.proyect.pensamiento_comp.model.Submission();
        submission1.setId(11L);
        entity1.setSubmission(submission1);

        Score entity2 = new Score();
        entity2.setId(2L);
        entity2.setPointsAwarded(20.0);
        entity2.setAssignmentDate(java.time.LocalDateTime.of(2023, 10, 29, 11, 0));
        com.proyect.pensamiento_comp.model.Submission submission2 = new com.proyect.pensamiento_comp.model.Submission();
        submission2.setId(12L);
        entity2.setSubmission(submission2);

        List<Score> entities = Arrays.asList(entity1, entity2);


        List<ScoreResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(10.0, dtos.get(0).getPointsAwarded());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 10, 0), dtos.get(0).getAssignmentDate());
        assertEquals(11L, dtos.get(0).getSubmissionId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(20.0, dtos.get(1).getPointsAwarded());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 11, 0), dtos.get(1).getAssignmentDate());
        assertEquals(12L, dtos.get(1).getSubmissionId());
    }

    @Test
    void testToEntity_minimalData() {

        ScoreCreateDTO dto = new ScoreCreateDTO();
        dto.setPointsAwarded(55.0);


        Score entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals(55.0, entity.getPointsAwarded());
        assertNull(entity.getId());
        assertNull(entity.getSubmission());
        assertNull(entity.getAssignmentDate());
    }
}
