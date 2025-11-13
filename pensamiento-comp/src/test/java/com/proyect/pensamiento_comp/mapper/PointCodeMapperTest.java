package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PointCodeCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PointCodeResponseDTO;
import com.proyect.pensamiento_comp.model.PointCode;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointCodeMapperTest {
    private final PointCodeMapper mapper = Mappers.getMapper(PointCodeMapper.class);

    @Test
    void testToEntity_fullData() {

        PointCodeCreateDTO dto = new PointCodeCreateDTO();
        dto.setCode("PCODE1");
        dto.setPoints(100);
        dto.setStatus("ACTIVE");
        dto.setUsageLimit(5);
        dto.setRedeemedAt(java.time.LocalDateTime.of(2025, 10, 29, 10, 0));
        dto.setActivityId(101L);
        dto.setStudentId(301L);


        PointCode entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("PCODE1", entity.getCode());
        assertEquals(100, entity.getPoints());
        assertEquals("ACTIVE", entity.getStatus());
        assertEquals(5, entity.getUsageLimit());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 10, 0), entity.getRedeemedAt());
        assertNull(entity.getActivity());
        assertNull(entity.getStudent());
    }

    @Test
    void testToDto_fullData() {

        PointCode entity = new PointCode();
        entity.setCode("PCODE2");
        entity.setPoints(200);
        entity.setStatus("USED");
        entity.setUsageLimit(10);
        entity.setRedeemedAt(java.time.LocalDateTime.of(2025, 10, 29, 11, 0));
        com.proyect.pensamiento_comp.model.Activity activity = new com.proyect.pensamiento_comp.model.Activity();
        activity.setId(102L);
        entity.setActivity(activity);
        com.proyect.pensamiento_comp.model.Student student = new com.proyect.pensamiento_comp.model.Student();
        student.setId(302L);
        entity.setStudent(student);


        PointCodeResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals("PCODE2", dto.getCode());
        assertEquals(200, dto.getPoints());
        assertEquals("USED", dto.getStatus());
        assertEquals(10, dto.getUsageLimit());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 11, 0), dto.getRedeemedAt());
        assertEquals(102L, dto.getActivityId());
        assertEquals(302L, dto.getStudentId());
    }

    @Test
    void testToDtoList_multipleElements() {

        PointCode entity1 = new PointCode();
        entity1.setCode("PC1");
        entity1.setPoints(10);
        entity1.setStatus("ACTIVE");
        entity1.setUsageLimit(1);
        entity1.setRedeemedAt(java.time.LocalDateTime.of(2025, 10, 29, 12, 0));
        com.proyect.pensamiento_comp.model.Activity activity1 = new com.proyect.pensamiento_comp.model.Activity();
        activity1.setId(111L);
        entity1.setActivity(activity1);
        com.proyect.pensamiento_comp.model.Student student1 = new com.proyect.pensamiento_comp.model.Student();
        student1.setId(311L);
        entity1.setStudent(student1);

        PointCode entity2 = new PointCode();
        entity2.setCode("PC2");
        entity2.setPoints(20);
        entity2.setStatus("USED");
        entity2.setUsageLimit(2);
        entity2.setRedeemedAt(java.time.LocalDateTime.of(2025, 10, 29, 13, 0));
        com.proyect.pensamiento_comp.model.Activity activity2 = new com.proyect.pensamiento_comp.model.Activity();
        activity2.setId(112L);
        entity2.setActivity(activity2);
        com.proyect.pensamiento_comp.model.Student student2 = new com.proyect.pensamiento_comp.model.Student();
        student2.setId(312L);
        entity2.setStudent(student2);

        List<PointCode> entities = Arrays.asList(entity1, entity2);


        List<PointCodeResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("PC1", dtos.get(0).getCode());
        assertEquals(10, dtos.get(0).getPoints());
        assertEquals("ACTIVE", dtos.get(0).getStatus());
        assertEquals(1, dtos.get(0).getUsageLimit());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 12, 0), dtos.get(0).getRedeemedAt());
        assertEquals(111L, dtos.get(0).getActivityId());
        assertEquals(311L, dtos.get(0).getStudentId());
        assertEquals("PC2", dtos.get(1).getCode());
        assertEquals(20, dtos.get(1).getPoints());
        assertEquals("USED", dtos.get(1).getStatus());
        assertEquals(2, dtos.get(1).getUsageLimit());
        assertEquals(java.time.LocalDateTime.of(2025, 10, 29, 13, 0), dtos.get(1).getRedeemedAt());
        assertEquals(112L, dtos.get(1).getActivityId());
        assertEquals(312L, dtos.get(1).getStudentId());
    }

    @Test
    void testToEntity_minimalCode() {

        PointCodeCreateDTO dto = new PointCodeCreateDTO();
        dto.setCode("MINCODE");


        PointCode entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("MINCODE", entity.getCode());
    }
}
