package com.proyect.pensamiento_comp.mapper;

import com.proyect.pensamiento_comp.dto.PeriodCreateDTO;
import com.proyect.pensamiento_comp.dto.response.PeriodResponseDTO;
import com.proyect.pensamiento_comp.model.Period;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeriodMapperTest {
    private final PeriodMapper mapper = Mappers.getMapper(PeriodMapper.class);

    @Test
    void testToEntity_fullData() {

        PeriodCreateDTO dto = new PeriodCreateDTO();
        dto.setCode("PER2023A");
        dto.setStartDate(LocalDate.of(2023, 1, 1));
        dto.setEndDate(LocalDate.of(2023, 1, 10));


        Period entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("PER2023A", entity.getCode());
        assertEquals(LocalDate.of(2023, 1, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2023, 1, 10), entity.getEndDate());
    }

    @Test
    void testToDto_fullData() {

        Period entity = new Period();
        entity.setId(2L);
        entity.setCode("PER2024B");
        entity.setStartDate(LocalDate.of(2024, 2, 2));
        entity.setEndDate(LocalDate.of(2024, 2, 12));


        PeriodResponseDTO dto = mapper.toDto(entity);


        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("PER2024B", dto.getCode());
        assertEquals(LocalDate.of(2024, 2, 2), dto.getStartDate());
        assertEquals(LocalDate.of(2024, 2, 12), dto.getEndDate());
    }

    @Test
    void testToDtoList_multipleElements() {

        Period entity1 = new Period();
        entity1.setId(1L);
        entity1.setCode("PER1");
        entity1.setStartDate(LocalDate.of(2023, 1, 1));
        entity1.setEndDate(LocalDate.of(2023, 1, 2));
        Period entity2 = new Period();
        entity2.setId(2L);
        entity2.setCode("PER2");
        entity2.setStartDate(LocalDate.of(2023, 2, 1));
        entity2.setEndDate(LocalDate.of(2023, 2, 2));
        List<Period> entities = Arrays.asList(entity1, entity2);


        List<PeriodResponseDTO> dtos = mapper.toDtoList(entities);


        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("PER1", dtos.get(0).getCode());
        assertEquals(LocalDate.of(2023, 1, 1), dtos.get(0).getStartDate());
        assertEquals(LocalDate.of(2023, 1, 2), dtos.get(0).getEndDate());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("PER2", dtos.get(1).getCode());
        assertEquals(LocalDate.of(2023, 2, 1), dtos.get(1).getStartDate());
        assertEquals(LocalDate.of(2023, 2, 2), dtos.get(1).getEndDate());
    }

    @Test
    void testToEntity_minimalCode() {

        PeriodCreateDTO dto = new PeriodCreateDTO();
        dto.setCode("MINCODE");


        Period entity = mapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals("MINCODE", entity.getCode());
        assertNull(entity.getId());
    }
}
