package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.StudentCreateDTO;
import com.proyect.pensamiento_comp.dto.response.StudentResponseDTO;
import com.proyect.pensamiento_comp.model.Student;
import com.proyect.pensamiento_comp.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {
    private final StudentMapper mapper = Mappers.getMapper(StudentMapper.class);

    @Test
    void testToEntity_fullData() {

        StudentCreateDTO dto = new StudentCreateDTO();
        dto.setCode("A123");
        dto.setGpa(4.5);
        dto.setUserId(10L);
        dto.setLevelId(30L);


        Student entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("A123", entity.getCode());
        assertEquals(4.5, entity.getGpa());

    }

    @Test
    void testToDto_fullData() {

        Student entity = new Student();
        entity.setId(2L);
        entity.setCode("B456");
        entity.setGpa(3.8);
        User user = new User();
        user.setId(11L);
        entity.setUser(user);

        StudentResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("B456", dto.getCode());
        assertEquals(3.8, dto.getGpa());
        assertEquals(11L, dto.getUserId());

    }

    @Test
    void testToDtoList_multipleElements() {

        Student entity1 = new Student();
        entity1.setId(1L);
        entity1.setCode("C789");
        entity1.setGpa(4.0);
        User user1 = new User();
        user1.setId(12L);
        entity1.setUser(user1);

        Student entity2 = new Student();
        entity2.setId(2L);
        entity2.setCode("D012");
        entity2.setGpa(3.5);
        User user2 = new User();
        user2.setId(13L);
        entity2.setUser(user2);

        List<Student> entities = Arrays.asList(entity1, entity2);


        List<StudentResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("C789", dtos.get(0).getCode());
        assertEquals(4.0, dtos.get(0).getGpa());
        assertEquals(12L, dtos.get(0).getUserId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("D012", dtos.get(1).getCode());
        assertEquals(3.5, dtos.get(1).getGpa());
        assertEquals(13L, dtos.get(1).getUserId());
    }

    @Test
    void testToEntity_minimalData() {

        StudentCreateDTO dto = new StudentCreateDTO();
        dto.setCode("E345");
        dto.setUserId(99L);


        Student entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("E345", entity.getCode());
        assertNull(entity.getId());
        assertEquals(99L, dto.getUserId());
    }
}
