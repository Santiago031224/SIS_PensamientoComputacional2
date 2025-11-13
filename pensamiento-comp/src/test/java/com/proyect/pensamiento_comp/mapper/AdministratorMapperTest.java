package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.AdministratorCreateDTO;
import com.proyect.pensamiento_comp.dto.response.AdministratorResponseDTO;
import com.proyect.pensamiento_comp.model.Administrator;
import com.proyect.pensamiento_comp.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorMapperTest {
    private final AdministratorMapper mapper = Mappers.getMapper(AdministratorMapper.class);

    @Test
    void testToEntity_fullData() {

        AdministratorCreateDTO dto = new AdministratorCreateDTO();
        dto.setDepartment("Sistemas");
        dto.setUserId(10L);


        Administrator entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Sistemas", entity.getDepartment());

    }

    @Test
    void testToDto_fullData() {

        Administrator entity = new Administrator();
        entity.setId(2L);
        entity.setDepartment("Recursos Humanos");
        User user = new User();
        user.setId(5L);
        entity.setUser(user);


        AdministratorResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Recursos Humanos", dto.getDepartment());
        assertEquals(5L, dto.getUserId());
    }

    @Test
    void testToDtoList_multipleElements() {

        Administrator entity1 = new Administrator();
        entity1.setId(1L);
        entity1.setDepartment("Dept1");
        User user1 = new User();
        user1.setId(11L);
        entity1.setUser(user1);
        Administrator entity2 = new Administrator();
        entity2.setId(2L);
        entity2.setDepartment("Dept2");
        User user2 = new User();
        user2.setId(12L);
        entity2.setUser(user2);
        List<Administrator> entities = Arrays.asList(entity1, entity2);


        List<AdministratorResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("Dept1", dtos.get(0).getDepartment());
        assertEquals(11L, dtos.get(0).getUserId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Dept2", dtos.get(1).getDepartment());
        assertEquals(12L, dtos.get(1).getUserId());
    }

    @Test
    void testToEntity_minimalDepartment() {

        AdministratorCreateDTO dto = new AdministratorCreateDTO();
        dto.setDepartment("Finanzas");


        Administrator entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("Finanzas", entity.getDepartment());
    }
}
