package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.UserCreateDTO;
import com.proyect.pensamiento_comp.dto.response.UserResponseDTO;
import com.proyect.pensamiento_comp.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity_fullData() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setName("Juan");
        dto.setLastName("Perez");
        dto.setEmail("juan.perez@icesi.edu.co");
        dto.setPassword("securepass");
        dto.setRoleIds(Set.of(1L, 2L));


        User entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Juan", entity.getName());
        assertEquals("Perez", entity.getLastName());
        assertEquals("juan.perez@icesi.edu.co", entity.getEmail());
        assertEquals("securepass", entity.getPassword());
        assertEquals("ACTIVE", entity.getStatus());
        assertNotNull(entity.getRoles());
        assertTrue(entity.getRoles().isEmpty());
        assertNull(entity.getProfilePicture());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getLastLogin());
    }

    @Test
    void testToDto_fullData() {

        User entity = new User();
        entity.setId(2L);
        entity.setName("Ana");
        entity.setLastName("Gomez");
        entity.setEmail("ana.gomez@icesi.edu.co");
        entity.setProfilePicture("pic.jpg");
        entity.setStatus(0);
        entity.setCreatedAt(java.time.LocalDateTime.of(2023, 10, 29, 12, 0));
        entity.setUpdatedAt(java.time.LocalDateTime.of(2023, 10, 29, 13, 0));
        entity.setLastLogin(java.time.LocalDateTime.of(2023, 10, 29, 14, 0));



        UserResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Ana", dto.getName());
        assertEquals("Gomez", dto.getLastName());
        assertEquals("ana.gomez@icesi.edu.co", dto.getEmail());
        assertEquals("pic.jpg", dto.getProfilePicture());
        assertEquals(0, dto.getStatus());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 12, 0), dto.getCreatedAt());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 13, 0), dto.getUpdatedAt());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 14, 0), dto.getLastLogin());
        assertNotNull(dto.getRoles());
        assertTrue(dto.getRoles().isEmpty());
    }

    @Test
    void testToDtoList_multipleElements() {

        User entity1 = new User();
        entity1.setId(1L);
        entity1.setName("U1");
        entity1.setLastName("L1");
        entity1.setEmail("u1@icesi.edu.co");
        entity1.setProfilePicture("pic1.jpg");
        entity1.setStatus(1);
        entity1.setCreatedAt(java.time.LocalDateTime.of(2023, 10, 29, 10, 0));
        entity1.setUpdatedAt(java.time.LocalDateTime.of(2023, 10, 29, 11, 0));
        entity1.setLastLogin(java.time.LocalDateTime.of(2023, 10, 29, 12, 0));

        User entity2 = new User();
        entity2.setId(2L);
        entity2.setName("U2");
        entity2.setLastName("L2");
        entity2.setEmail("u2@icesi.edu.co");
        entity2.setProfilePicture("pic2.jpg");
        entity2.setStatus(1);
        entity2.setCreatedAt(java.time.LocalDateTime.of(2023, 10, 29, 13, 0));
        entity2.setUpdatedAt(java.time.LocalDateTime.of(2023, 10, 29, 14, 0));
        entity2.setLastLogin(java.time.LocalDateTime.of(2023, 10, 29, 15, 0));

        List<User> entities = Arrays.asList(entity1, entity2);


        List<UserResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("U1", dtos.get(0).getName());
        assertEquals("L1", dtos.get(0).getLastName());
        assertEquals("u1@icesi.edu.co", dtos.get(0).getEmail());
        assertEquals("pic1.jpg", dtos.get(0).getProfilePicture());
        assertEquals("ACTIVE", dtos.get(0).getStatus());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 10, 0), dtos.get(0).getCreatedAt());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 11, 0), dtos.get(0).getUpdatedAt());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 12, 0), dtos.get(0).getLastLogin());
        assertNotNull(dtos.get(0).getRoles());
        assertTrue(dtos.get(0).getRoles().isEmpty());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("U2", dtos.get(1).getName());
        assertEquals("L2", dtos.get(1).getLastName());
        assertEquals("u2@icesi.edu.co", dtos.get(1).getEmail());
        assertEquals("pic2.jpg", dtos.get(1).getProfilePicture());
        assertEquals("INACTIVE", dtos.get(1).getStatus());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 13, 0), dtos.get(1).getCreatedAt());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 14, 0), dtos.get(1).getUpdatedAt());
        assertEquals(java.time.LocalDateTime.of(2023, 10, 29, 15, 0), dtos.get(1).getLastLogin());
        assertNotNull(dtos.get(1).getRoles());
        assertTrue(dtos.get(1).getRoles().isEmpty());
    }

    @Test
    void testToEntity_minimalData() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("minimal@icesi.edu.co");
        dto.setPassword("minpass");


        User entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("minimal@icesi.edu.co", entity.getEmail());
        assertEquals("minpass", entity.getPassword());
        assertNull(entity.getId());
    }
}
