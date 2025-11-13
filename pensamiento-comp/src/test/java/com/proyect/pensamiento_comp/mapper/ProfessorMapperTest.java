package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.ProfessorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.ProfessorResponseDTO;
import com.proyect.pensamiento_comp.model.Professor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorMapperTest {
    private final ProfessorMapper mapper = Mappers.getMapper(ProfessorMapper.class);

    @Test
    void testToEntity_fullData() {

        ProfessorCreateDTO dto = new ProfessorCreateDTO();
        dto.setRegistrationDate(java.time.LocalDate.of(2025, 10, 29));
        dto.setOfficeLocation("A-101");
        dto.setSpecialtyArea("Mathematics");
        dto.setMaxGroupsAllowed(3);
        dto.setAcademicRank("Associate");
        dto.setRatingAverage(4.7);
        dto.setUserId(10L);


        Professor entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("A-101", entity.getOfficeLocation());
        assertEquals("Mathematics", entity.getSpecialtyArea());
        assertEquals(3, entity.getMaxGroupsAllowed());
        assertEquals("Associate", entity.getAcademicRank());
    }

    @Test
    void testToDto_fullData() {

        Professor entity = new Professor();
        entity.setId(2L);
        entity.setOfficeLocation("B-202");
        entity.setSpecialtyArea("Physics");
        entity.setMaxGroupsAllowed(5);
        entity.setAcademicRank("Full");
        com.proyect.pensamiento_comp.model.User user = new com.proyect.pensamiento_comp.model.User();
        user.setId(20L);
        entity.setUser(user);


        ProfessorResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(java.time.LocalDate.of(2025, 10, 29), dto.getRegistrationDate());
        assertEquals("B-202", dto.getOfficeLocation());
        assertEquals("Physics", dto.getSpecialtyArea());
        assertEquals(5, dto.getMaxGroupsAllowed());
        assertEquals("Full", dto.getAcademicRank());
        assertEquals(3.9, dto.getRatingAverage());
        assertEquals(20L, dto.getUserId());
    }

    @Test
    void testToDtoList_multipleElements() {

        Professor entity1 = new Professor();
        entity1.setId(1L);
        entity1.setOfficeLocation("C-303");
        entity1.setSpecialtyArea("Chemistry");
        entity1.setMaxGroupsAllowed(2);
        entity1.setAcademicRank("Assistant");
        com.proyect.pensamiento_comp.model.User user1 = new com.proyect.pensamiento_comp.model.User();
        user1.setId(21L);
        entity1.setUser(user1);

        Professor entity2 = new Professor();
        entity2.setId(2L);
        entity2.setOfficeLocation("D-404");
        entity2.setSpecialtyArea("Biology");
        entity2.setMaxGroupsAllowed(4);
        entity2.setAcademicRank("Full");
        com.proyect.pensamiento_comp.model.User user2 = new com.proyect.pensamiento_comp.model.User();
        user2.setId(22L);
        entity2.setUser(user2);

        List<Professor> entities = Arrays.asList(entity1, entity2);


        List<ProfessorResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(java.time.LocalDate.of(2025, 10, 28), dtos.get(0).getRegistrationDate());
        assertEquals("C-303", dtos.get(0).getOfficeLocation());
        assertEquals("Chemistry", dtos.get(0).getSpecialtyArea());
        assertEquals(2, dtos.get(0).getMaxGroupsAllowed());
        assertEquals("Assistant", dtos.get(0).getAcademicRank());
        assertEquals(4.2, dtos.get(0).getRatingAverage());
        assertEquals(21L, dtos.get(0).getUserId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(java.time.LocalDate.of(2025, 10, 27), dtos.get(1).getRegistrationDate());
        assertEquals("D-404", dtos.get(1).getOfficeLocation());
        assertEquals("Biology", dtos.get(1).getSpecialtyArea());
        assertEquals(4, dtos.get(1).getMaxGroupsAllowed());
        assertEquals("Full", dtos.get(1).getAcademicRank());
        assertEquals(3.5, dtos.get(1).getRatingAverage());
        assertEquals(22L, dtos.get(1).getUserId());
    }

}
