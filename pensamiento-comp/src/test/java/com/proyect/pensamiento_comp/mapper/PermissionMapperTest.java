package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PermissionDTO;
import com.proyect.pensamiento_comp.dto.response.PermissionResponseDTO;
import com.proyect.pensamiento_comp.model.Permission;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PermissionMapperTest {
    private final PermissionMapper mapper = Mappers.getMapper(PermissionMapper.class);

    @Test
    void testToEntity_fullData() {

        PermissionDTO dto = new PermissionDTO();
        dto.setName("PERM_READ");
        dto.setDescription("Puede leer datos");


        Permission entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId()); // id is ignored on creation
        assertEquals("PERM_READ", entity.getName());
        assertEquals("Puede leer datos", entity.getDescription());
    }

    @Test
    void testToDto_fullData() {

        Permission entity = new Permission();
        entity.setId(2L);
        entity.setName("PERM_WRITE");
        entity.setDescription("Puede escribir datos");


        PermissionResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("PERM_WRITE", dto.getName());
        assertEquals("Puede escribir datos", dto.getDescription());
    }

    @Test
    void testToDtoList_multipleElements() {

        Permission entity1 = new Permission();
        entity1.setId(1L);
        entity1.setName("PERM_A");
        entity1.setDescription("A");
        Permission entity2 = new Permission();
        entity2.setId(2L);
        entity2.setName("PERM_B");
        entity2.setDescription("B");
        List<Permission> entities = Arrays.asList(entity1, entity2);


        List<PermissionResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("PERM_A", dtos.get(0).getName());
        assertEquals("A", dtos.get(0).getDescription());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("PERM_B", dtos.get(1).getName());
        assertEquals("B", dtos.get(1).getDescription());
    }

    @Test
    void testToEntity_minimalName() {

        PermissionDTO dto = new PermissionDTO();
        dto.setName("PERM_MIN");


        Permission entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("PERM_MIN", entity.getName());
        assertNull(entity.getId());
    }
}
