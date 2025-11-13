package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.GroupCreateDTO;
import com.proyect.pensamiento_comp.dto.response.GroupResponseDTO;
import com.proyect.pensamiento_comp.model.Group;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupMapperTest {
    private final GroupMapper mapper = Mappers.getMapper(GroupMapper.class);

    @Test
    void testToEntity_fullData() {

        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Grupo 1");
        dto.setPeriodId(7L);


        Group entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Grupo 1", entity.getName());
        if (entity.getPeriod() != null) {
            assertEquals(7L, entity.getPeriod().getId());
        }
    }

    @Test
    void testToDto_fullData() {

        Group entity = new Group();
        entity.setId(2L);
        entity.setName("G2");



        GroupResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("G2", dto.getName());
    }

    @Test
    void testToDtoList_multipleElements() {

        Group entity1 = new Group();
        entity1.setId(1L);
        entity1.setName("G1");
        Group entity2 = new Group();
        entity2.setId(2L);
        entity2.setName("G2");
        List<Group> entities = Arrays.asList(entity1, entity2);


        List<GroupResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("G1", dtos.get(0).getName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("G2", dtos.get(1).getName());
    }

    @Test
    void testToEntity_minimalName() {

        GroupCreateDTO dto = new GroupCreateDTO();
        dto.setName("Solo nombre");


        Group entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Solo nombre", entity.getName());
    }
}
