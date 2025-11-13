package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.RoleCreateDTO;
import com.proyect.pensamiento_comp.dto.response.RoleResponseDTO;
import com.proyect.pensamiento_comp.model.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {
    private final RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

    @Test
    void testToEntity_fullData() {

        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setName("ROLE_ADMIN");
        dto.setPermissionIds(Set.of(1L, 2L));


        Role entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("ROLE_ADMIN", entity.getName());

        assertTrue(entity.getPermissions() == null || entity.getPermissions().isEmpty());
        assertTrue(entity.getUsers() == null || entity.getUsers().isEmpty());
    }

    @Test
    void testToDto_fullData() {

        Role entity = new Role();
        entity.setId(2L);
        entity.setName("ROLE_USER");


        RoleResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("ROLE_USER", dto.getName());

    }

    @Test
    void testToDtoList_multipleElements() {

        Role entity1 = new Role();
        entity1.setId(1L);
        entity1.setName("R1");
        Role entity2 = new Role();
        entity2.setId(2L);
        entity2.setName("R2");
        List<Role> entities = Arrays.asList(entity1, entity2);


        List<RoleResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("R1", dtos.get(0).getName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("R2", dtos.get(1).getName());
    }

    @Test
    void testToEntity_minimalName() {

        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setName("ROLE_MIN");


        Role entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("ROLE_MIN", entity.getName());
        assertNull(entity.getId());
    }
}
